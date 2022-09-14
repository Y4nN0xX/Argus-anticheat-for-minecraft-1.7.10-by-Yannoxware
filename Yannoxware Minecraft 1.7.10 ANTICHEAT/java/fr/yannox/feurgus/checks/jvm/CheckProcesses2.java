package fr.yannox.feurgus.checks.jvm;

import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.in.PacketInCrash;
import java.lang.management.ManagementFactory;
import java.util.List;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;

public class CheckProcesses2
extends ACheck {
    private final long processId = this.getProcessId();
    private final String[] flaggedNames = new String[]{"clumsy", "xrayinjector", "privatevpn", "vpndaemon", "agent_ovpnconnect_", "vyprvpn"};

    public CheckProcesses2(InternalSession session) {
        super(session);
    }

    private void checkProcesses() {
        try {
            List processes = new SystemInfo().getOperatingSystem().getProcesses();
            for (OSProcess process : processes) {
                String name;
                if (process == null || (name = process.getName()) == null) continue;
                for (String fl : this.flaggedNames) {
                    if (fl == null || fl.isEmpty() || !name.toLowerCase().contains(fl)) continue;
                    new PacketInCrash().crash();
                    return;
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
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

    @Override
    public void runCheck(long occurrences) {
        this.checkProcesses();
    }

    @Override
    public int getRepeatTime() {
        return 30;
    }
}
