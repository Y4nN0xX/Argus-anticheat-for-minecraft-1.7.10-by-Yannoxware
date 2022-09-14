package fr.yannox.feurgus.connection.packets.out;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.yannox.feurgus.connection.packets.PacketOut;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;
import java.util.Map;

public class PacketOutEnvVariables
extends PacketOut {
    private final Map<String, String> envVar;

    public PacketOutEnvVariables(Map<String, String> envVar) {
        this.envVar = envVar;
    }

    @Override
    public int getPacketId() {
        return 54;
    }

    @Override
    public byte[] toBytes() {
        if (this.envVar == null) {
            return null;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeInt(this.envVar.size());
        for (Map.Entry<String, String> entry : this.envVar.entrySet()) {
            out.writeByte(3);
            out.writeUTF(entry.getKey());
            out.writeByte(19);
            out.writeUTF(entry.getValue());
        }
        try {
            MyByteArrayDataOutput arrayOutputStream = new MyByteArrayDataOutput();
            byte[] array = out.toByteArray();
            arrayOutputStream.writeInt(array.length);
            arrayOutputStream.write(array);
            return arrayOutputStream.toByteArray();
        }
        catch (Exception error) {
            return null;
        }
    }
}
