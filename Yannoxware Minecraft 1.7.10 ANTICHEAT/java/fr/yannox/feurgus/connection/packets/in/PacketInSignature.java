package fr.yannox.feurgus.connection.packets.in;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.in.PacketIn;
import fr.yannox.feurgus.connection.packets.out.PacketOutSignature;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.CRC32;
import java.util.zip.Inflater;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

public class PacketInSignature
extends PacketIn {
    @Override
    public int getPacketId() {
        return 3;
    }

    @Override
    public void fromBytes(byte[] bytes) {
        byte[] finalDecompressed;
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String uniqueId = in.readUTF();
        int count = in.readInt();
        if (count <= 0 || (long)count >= 8000L) {
            return;
        }
        int compCount = in.readInt();
        if (compCount <= 0 || compCount >= Short.MAX_VALUE) {
            return;
        }
        try {
            byte[] compressed = new byte[compCount];
            in.readFully(compressed);
            byte[] decompressed = new byte[Short.MAX_VALUE];
            Inflater decompressor = new Inflater();
            decompressor.setInput(compressed);
            int size = decompressor.inflate(decompressed);
            finalDecompressed = new byte[size];
            System.arraycopy(decompressed, 0, finalDecompressed, 0, size);
            decompressor.end();
        }
        catch (Exception error) {
            error.printStackTrace();
            return;
        }
        ByteArrayDataInput output = ByteStreams.newDataInput(finalDecompressed);
        ArrayList<String> signatures = new ArrayList<String>();
        for (int i = 0; i < count; ++i) {
            String name = output.readUTF();
            try {
                String cPath = name.replace(".", "/") + ".class";
                ClassReader cr = new ClassReader(name.replace(".", "/"));
                StringWriter writer = new StringWriter();
                TraceClassVisitor visitor = new TraceClassVisitor(new PrintWriter(writer));
                cr.accept(visitor, 0);
                byte[] bytecode = writer.toString().getBytes();
                String crc = this.getCRC32(bytecode);
                signatures.add(crc);
                continue;
            }
            catch (Exception error) {
                signatures.add("-");
            }
        }
        PacketOutSignature signature = new PacketOutSignature(uniqueId, signatures);
        if (InternalSession.instance != null) {
            try {
                InternalSession.instance.sendPacket(signature);
            }
            catch (Exception error) {
                error.printStackTrace();
            }
        }
    }

    private String getCRC32(byte[] data) {
        CRC32 fileCRC32 = new CRC32();
        fileCRC32.update(data);
        return String.format(Locale.US, "%08X", fileCRC32.getValue());
    }
}
