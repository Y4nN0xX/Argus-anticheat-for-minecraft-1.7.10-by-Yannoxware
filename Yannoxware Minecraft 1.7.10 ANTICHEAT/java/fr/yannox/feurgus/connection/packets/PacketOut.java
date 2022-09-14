package fr.yannox.feurgus.connection.packets;

import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.utils.LongUtils;
import java.io.ByteArrayOutputStream;
import java.util.Map;

public abstract class PacketOut {
    public abstract int getPacketId();

    public abstract byte[] toBytes();

    public byte[] generatePacket() throws Exception {
        byte[] data;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(23);
        stream.write((byte)this.getPacketId());
        if (InternalSession.instance != null) {
            Map.Entry<Long, byte[]> entry = InternalSession.instance.networkKey.generateKey();
            stream.write(LongUtils.longToBytes(entry.getKey()));
            stream.write(entry.getValue());
        }
        if ((data = this.toBytes()) == null) {
            return null;
        }
        stream.write(data);
        return stream.toByteArray();
    }
}
