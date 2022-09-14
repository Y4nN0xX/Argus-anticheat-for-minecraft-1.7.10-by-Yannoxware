package fr.yannox.feurgus.connection.packets.out;

import fr.yannox.feurgus.connection.packets.PacketOut;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;
import java.util.List;

public class PacketOutSignature
extends PacketOut {
    private final String requestId;
    private final List<String> signatures;

    public PacketOutSignature(String requestId, List<String> signatures) {
        this.requestId = requestId;
        this.signatures = signatures;
    }

    @Override
    public int getPacketId() {
        return 25;
    }

    private byte[] generateOutput() {
        MyByteArrayDataOutput output = new MyByteArrayDataOutput();
        output.writeByte((byte)43);
        output.writeUTF(this.requestId);
        output.writeByte((byte)28);
        output.writeInt(this.signatures.size());
        output.writeByte((byte)25);
        this.signatures.forEach(output::writeUTF);
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
