package fr.yannox.feurgus.connection.packets.out;

import fr.yannox.feurgus.connection.packets.PacketOut;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;
import java.util.Set;

public class PacketOutAltSession
extends PacketOut {
    private Set<String> a;
    private Set<String> b;

    public PacketOutAltSession() {
    }

    public PacketOutAltSession(Set<String> a, Set<String> b) {
        this.a = a;
        this.b = b;
    }

    private byte[] generate() {
        MyByteArrayDataOutput arrayOutputStream = new MyByteArrayDataOutput();
        arrayOutputStream.writeByte((byte)3);
        arrayOutputStream.writeInt(this.a.size());
        this.a.forEach(arrayOutputStream::writeUTF);
        arrayOutputStream.writeByte((byte)76);
        arrayOutputStream.writeInt(this.b.size());
        this.b.forEach(arrayOutputStream::writeUTF);
        return arrayOutputStream.toByteArray();
    }

    @Override
    public int getPacketId() {
        return 6;
    }

    @Override
    public byte[] toBytes() {
        try {
            MyByteArrayDataOutput arrayOutputStream = new MyByteArrayDataOutput();
            byte[] raw = this.generate();
            arrayOutputStream.writeInt(raw.length);
            arrayOutputStream.write(raw);
            return arrayOutputStream.toByteArray();
        }
        catch (Exception error) {
            return null;
        }
    }
}
