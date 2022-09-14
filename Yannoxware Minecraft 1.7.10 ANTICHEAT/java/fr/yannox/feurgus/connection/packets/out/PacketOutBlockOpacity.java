package fr.yannox.feurgus.connection.packets.out;

import fr.yannox.feurgus.connection.packets.PacketOut;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;

public class PacketOutBlockOpacity
extends PacketOut {
    private final byte[] a;

    public PacketOutBlockOpacity(byte[] a) {
        this.a = a;
    }

    @Override
    public int getPacketId() {
        return 13;
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
            return null;
        }
    }
}
