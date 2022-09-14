package fr.yannox.feurgus.connection.packets.out;

import fr.yannox.feurgus.connection.packets.PacketOut;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;
import java.lang.management.ManagementFactory;

public class PacketOutProcess
extends PacketOut {
    private int chunkId;
    private int chunkSize;
    private byte[] a;

    public PacketOutProcess(int chunkId, int chunkSize, int processCount, byte[] processBytes) {
        this.chunkId = chunkId;
        this.chunkSize = chunkSize;
        this.convertToBytes(processCount, processBytes);
    }

    private void convertToBytes(int processCount, byte[] processBytes) {
        MyByteArrayDataOutput out = new MyByteArrayDataOutput();
        out.writeInt(this.chunkId);
        out.writeInt(this.chunkSize);
        out.writeLong(this.getProcessId());
        out.writeInt(processCount);
        out.write(processBytes);
        this.a = out.toByteArray();
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
    public int getPacketId() {
        return 5;
    }

    @Override
    public byte[] toBytes() {
        try {
            MyByteArrayDataOutput arrayOutputStream = new MyByteArrayDataOutput();
            arrayOutputStream.writeInt(this.a.length);
            arrayOutputStream.write(this.a);
            return arrayOutputStream.toByteArray();
        }
        catch (Exception error) {
            error.printStackTrace();
            return null;
        }
    }
}
