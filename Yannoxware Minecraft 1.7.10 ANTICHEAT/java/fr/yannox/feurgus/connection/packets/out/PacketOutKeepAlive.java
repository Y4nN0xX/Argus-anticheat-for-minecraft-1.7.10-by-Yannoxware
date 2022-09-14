package fr.yannox.feurgus.connection.packets.out;

import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.PacketOut;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;

public class PacketOutKeepAlive
extends PacketOut {
    private final byte[] a;

    public PacketOutKeepAlive(byte[] a) {
        this.a = a;
    }

    @Override
    public int getPacketId() {
        return 7;
    }

    private byte[] generateOutput() {
        MyByteArrayDataOutput output = new MyByteArrayDataOutput();
        output.writeByte((byte)30);
        output.writeInt(InternalSession.instance.networkKey.getKeepAliveKey().length);
        output.writeByte((byte)44);
        output.write(InternalSession.instance.networkKey.getKeepAliveKey());
        output.writeByte((byte)65);
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
