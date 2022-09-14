package fr.yannox.feurgus.checks.jvm;

import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.out.PacketOutProcess;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;
import fr.yannox.feurgus.utils.threading.ThreadStarter;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oshi.SystemInfo;
import oshi.driver.windows.wmi.Win32ProcessCached;
import oshi.software.os.OSProcess;

public class CheckProcesses
extends ACheck {
    private long processId = this.getProcessId();
    private boolean oshiWorking = true;

    public CheckProcesses(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    private void checkProcesses() {
        if (!this.oshiWorking) {
            return;
        }
        try {
            List processes = new SystemInfo().getOperatingSystem().getProcesses();
            int i = 0;
            MyByteArrayDataOutput globalOutput = new MyByteArrayDataOutput();
            HashMap<byte[], Integer> processPackets = new HashMap<byte[], Integer>();
            Iterator iterator = processes.iterator();
            while (iterator.hasNext()) {
                OSProcess process;
                byte[] data = this.convertToBytes(process, (long)(process = (OSProcess)iterator.next()).getProcessID() == this.getProcessId());
                if (globalOutput.toByteArray().length + data.length >= 2048) {
                    processPackets.put(globalOutput.toByteArray(), i);
                    i = 0;
                    globalOutput = new MyByteArrayDataOutput();
                }
                globalOutput.write(data);
                ++i;
            }
            if (i > 0) {
                processPackets.put(globalOutput.toByteArray(), i);
            }
            int j = 0;
            for (Map.Entry entry : processPackets.entrySet()) {
                this.sendPacket(new PacketOutProcess(j, processPackets.size(), (Integer)entry.getValue(), (byte[])entry.getKey()));
                try {
                    Thread.sleep(200L);
                }
                catch (Exception error) {
                    error.printStackTrace();
                }
                ++j;
            }
        }
        catch (Exception error) {
            this.oshiWorking = false;
        }
    }

    private long getProcessId() {
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        int index = jvmName.indexOf(64);
        if (index < 1) {
            return -1L;
        }
        try {
            return Long.parseLong(jvmName.substring(0, index));
        }
        catch (NumberFormatException numberFormatException) {
            return -1L;
        }
    }

    private byte[] convertToBytes(OSProcess process, boolean full) {
        MyByteArrayDataOutput out = new MyByteArrayDataOutput();
        if (full) {
            out.write((byte)1);
        } else {
            out.write((byte)0);
        }
        out.writeInt(process.getProcessID());
        out.writeUTF(process.getName());
        if (full) {
            out.writeUTF(process.getUser());
            out.writeUTF(process.getPath());
            String commandLine = Win32ProcessCached.getInstance().getCommandLine(process.getProcessID(), process.getStartTime());
            commandLine = this.replaceCommandLine(commandLine);
            out.writeUTF(commandLine);
            out.writeLong(process.getUpTime());
            out.writeLong(process.getAffinityMask());
            out.writeLong(process.getPriority());
            out.writeUTF(process.getCurrentWorkingDirectory());
            out.writeInt(process.getParentProcessID());
            out.writeInt(process.getThreadCount());
        }
        return out.toByteArray();
    }

    private String replaceCommandLine(String commandLine) {
        Pattern pattern = Pattern.compile("--accessToken \\s*([^ ]*)");
        Matcher matcher = pattern.matcher(commandLine);
        if (matcher.find()) {
            commandLine = matcher.replaceFirst("--accessToken XXXXXXXXXX");
        }
        return commandLine;
    }

    @Override
    public void runCheck(long occurrences) {
        this.checkProcesses();
    }

    @Override
    public int getRepeatTime() {
        return 300;
    }
}
