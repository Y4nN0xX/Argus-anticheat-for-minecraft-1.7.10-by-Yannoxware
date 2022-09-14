package fr.yannox.feurgus.connection.packets.out;

import fr.yannox.feurgus.connection.packets.PacketOut;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;

public class PacketOutAttachedModuleInfo
extends PacketOut {
    private final String error;
    private final String lib;
    private final int i;
    private final int max;
    private final byte[] a;

    public PacketOutAttachedModuleInfo(String lib, String error) {
        this.error = error;
        this.lib = lib;
        this.i = -1;
        this.max = -1;
        this.a = null;
    }

    public PacketOutAttachedModuleInfo(String lib, int i, int max, byte[] a) {
        this.error = null;
        this.lib = lib;
        this.i = i;
        this.max = max;
        this.a = a;
    }

    @Override
    public int getPacketId() {
        return 27;
    }

    private byte[] generateOutput() {
        MyByteArrayDataOutput output = new MyByteArrayDataOutput();
        output.writeByte((byte)66);
        if (this.error != null && this.a != null) {
            output.writeByte((byte)51);
            output.writeUTF(this.lib);
            output.writeByte((byte)27);
            output.writeInt(this.i);
            output.writeByte((byte)24);
            output.writeInt(this.max);
            output.writeByte((byte)17);
            output.writeInt(this.a.length);
            output.writeByte((byte)33);
            output.write(this.a);
        } else {
            output.writeByte((byte)-124);
            output.writeUTF(this.lib);
            output.writeByte((byte)4);
            output.writeUTF(this.error);
        }
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
