package fr.yannox.feurgus.connection.packets.out;

import fr.yannox.feurgus.connection.packets.PacketOut;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;

public class PacketOutMCScreenshot
extends PacketOut {
    private final int i;
    private final int max;
    private final byte[] a;

    public PacketOutMCScreenshot(int i, int max, byte[] a) {
        this.i = i;
        this.max = max;
        this.a = a;
    }

    @Override
    public int getPacketId() {
        return 24;
    }

    private byte[] generateOutput() {
        MyByteArrayDataOutput output = new MyByteArrayDataOutput();
        output.writeByte((byte)38);
        output.writeInt(this.i);
        output.writeByte((byte)20);
        output.writeInt(this.max);
        output.writeByte((byte)17);
        output.writeInt(this.a.length);
        output.write(this.a);
        return output.toByteArray();
    }

    @Override
    public byte[] toBytes() {
        try {
            byte[] a = this.generateOutput();
            MyByteArrayDataOutput arrayOutputStream = new MyByteArrayDataOutput();
            arrayOutputStream.writeInt(a.length);
            arrayOutputStream.write(a);
            return arrayOutputStream.toByteArray();
        }
        catch (Exception error) {
            return null;
        }
    }
}
