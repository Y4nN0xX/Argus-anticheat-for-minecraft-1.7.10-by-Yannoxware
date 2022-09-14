package fr.yannox.feurgus.checks.kernel;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.out.PacketOutAttachedModules;
import fr.yannox.feurgus.utils.OSUtils;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;
import fr.yannox.feurgus.utils.threading.ThreadStarter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Locale;
import java.util.zip.CRC32;

public class CheckAttachedModules
extends ACheck {
    public CheckAttachedModules(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    @Override
    public void runCheck(long occurrences) {
        try {
            this.sendPacket(new PacketOutAttachedModules(this.findAttachedModules()));
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    @Override
    public int getRepeatTime() {
        return 230;
    }

    private byte[] findAttachedModules() {
        MyByteArrayDataOutput output = new MyByteArrayDataOutput();
        if (!OSUtils.isWindows()) {
            output.write(22);
            return output.toByteArray();
        }
        output.write(52);
        long pid = this.getProcessId();
        if (pid == -1L) {
            return null;
        }
        Psapi api = Psapi.INSTANCE;
        Kernel32 kernel = Kernel32.INSTANCE;
        WinNT.HANDLE hd = kernel.OpenProcess(1024, false, (int)this.getProcessId());
        if (hd == null) {
            return null;
        }
        WinDef.HMODULE[] mdl = new WinDef.HMODULE[1024];
        IntByReference ref = new IntByReference();
        List mod = Kernel32Util.getModules((int)((int)this.getProcessId()));
        output.writeInt(mod.size());
        for (Tlhelp32.MODULEENTRY32W module : mod) {
            output.writeUTF(module.szExePath());
            String path = module.szExePath();
            try {
                File f = new File(path);
                String c = this.getCRC32(f);
                output.writeUTF(c);
            }
            catch (Exception error) {
                output.writeUTF("NONE");
            }
        }
        return output.toByteArray();
    }

    private String getCRC32(File file) throws Exception {
        int bytesRead;
        FileInputStream in = new FileInputStream(file);
        CRC32 crcMaker = new CRC32();
        int size = 65536;
        byte[] buffer = new byte[65536];
        while ((bytesRead = ((InputStream)in).read(buffer)) != -1) {
            crcMaker.update(buffer, 0, bytesRead);
        }
        long value = crcMaker.getValue();
        return String.format(Locale.US, "%08X", value);
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
}
