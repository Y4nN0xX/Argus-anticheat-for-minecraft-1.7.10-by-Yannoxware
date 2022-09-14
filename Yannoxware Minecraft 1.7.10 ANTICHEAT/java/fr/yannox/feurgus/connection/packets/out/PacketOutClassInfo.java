package fr.yannox.feurgus.connection.packets.out;

import fr.yannox.feurgus.connection.packets.PacketOut;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;

public class PacketOutClassInfo
extends PacketOut {
    private final String className;
    private final int i;
    private final int max;
    private final byte[] a;

    public PacketOutClassInfo(String className, int i, int max, byte[] a) {
        this.className = className;
        this.i = i;
        this.max = max;
        this.a = a;
    }

    @Override
    public int getPacketId() {
        return 26;
    }

    private byte[] generateOutput() {
        MyByteArrayDataOutput output = new MyByteArrayDataOutput();
        output.writeByte((byte)8);
        output.writeInt(this.i);
        output.writeByte((byte)22);
        output.writeInt(this.max);
        output.writeByte((byte)50);
        output.writeUTF(this.className);
        if (this.a == null) {
            output.writeByte((byte)0);
            return output.toByteArray();
        }
        output.writeByte((byte)1);
        output.writeByte((byte)43);
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
