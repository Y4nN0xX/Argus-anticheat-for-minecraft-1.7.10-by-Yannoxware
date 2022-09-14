package fr.yannox.feurgus.connection.packets.out;

import fr.yannox.feurgus.connection.packets.PacketOut;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;

public class PacketOutLogin
extends PacketOut {
    private final byte[] baseDynamicKey;
    private final long[] uuidBits;

    public PacketOutLogin(byte[] baseDynamicKey, long[] uuidBits) {
        this.baseDynamicKey = baseDynamicKey;
        this.uuidBits = uuidBits;
    }

    @Override
    public int getPacketId() {
        return 69;
    }

    private byte[] generateOutput() {
        MyByteArrayDataOutput output = new MyByteArrayDataOutput();
        output.writeByte((byte)60);
        output.writeInt(this.baseDynamicKey.length);
        output.writeByte((byte)18);
        output.write(this.baseDynamicKey);
        output.writeByte((byte)42);
        output.writeLong(this.uuidBits[0]);
        output.writeByte((byte)63);
        output.writeLong(this.uuidBits[1]);
        output.writeByte((byte)55);
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
