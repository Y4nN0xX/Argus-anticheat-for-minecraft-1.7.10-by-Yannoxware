package fr.yannox.feurgus.connection.packets.out;

import fr.yannox.feurgus.connection.packets.PacketOut;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;

public class PacketOutRealIp
extends PacketOut {
    private final byte[] a;

    public PacketOutRealIp(byte[] a) {
        this.a = a;
    }

    @Override
    public int getPacketId() {
        return 9;
    }

    @Override
    public byte[] toBytes() {
        if (this.a == null) {
            return null;
        }
        try {
            MyByteArrayDataOutput arrayOutputStream = new MyByteArrayDataOutput();
            arrayOutputStream.writeInt(this.a.length);
            arrayOutputStream.write(this.a);
            return arrayOutputStream.toByteArray();
        }
        catch (Exception error) {
            return null;
        }
    }
}
