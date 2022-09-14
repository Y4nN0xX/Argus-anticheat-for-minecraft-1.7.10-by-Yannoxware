package fr.yannox.feurgus.connection.packets.out;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.yannox.feurgus.connection.packets.PacketOut;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;
import java.util.Map;

public class PacketOutPalaSolo
extends PacketOut {
    private final Map<String, Long> names;

    public PacketOutPalaSolo(Map<String, Long> names) {
        this.names = names;
    }

    @Override
    public int getPacketId() {
        return 14;
    }

    private byte[] generateOutput() {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeInt(this.names.size());
        for (Map.Entry<String, Long> entry : this.names.entrySet()) {
            output.writeUTF(entry.getKey());
            output.writeLong(entry.getValue());
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
