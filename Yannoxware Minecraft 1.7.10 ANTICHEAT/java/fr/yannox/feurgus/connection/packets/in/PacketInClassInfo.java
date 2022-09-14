package fr.yannox.feurgus.connection.packets.in;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.in.PacketIn;
import fr.yannox.feurgus.connection.packets.out.PacketOutClassInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.objectweb.asm.ClassReader;

public class PacketInClassInfo
extends PacketIn {
    @Override
    public int getPacketId() {
        return 4;
    }

    @Override
    public void fromBytes(byte[] bytes) {
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String className = in.readUTF();
        new Thread(() -> {
            block8: {
                try {
                    ClassReader cr = new ClassReader(className.replace(".", "/"));
                    List<byte[]> chunks = this.divideArray(cr.b, 4096);
                    int max = chunks.size();
                    for (int i = 0; i < chunks.size(); ++i) {
                        PacketOutClassInfo packet = new PacketOutClassInfo(className, i, max, chunks.get(i));
                        if (InternalSession.instance != null) {
                            try {
                                InternalSession.instance.sendPacket(packet);
                            }
                            catch (Exception exception) {
                                // empty catch block
                            }
                        }
                        Thread.sleep(1000L);
                    }
                }
                catch (Exception error) {
                    PacketOutClassInfo packet = new PacketOutClassInfo(className, 0, 0, null);
                    if (InternalSession.instance == null) break block8;
                    try {
                        InternalSession.instance.sendPacket(packet);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
        }).start();
    }

    private List<byte[]> divideArray(byte[] source, int chunksize) {
        ArrayList<byte[]> result = new ArrayList<byte[]>();
        for (int start = 0; start < source.length; start += chunksize) {
            int end = Math.min(source.length, start + chunksize);
            result.add(Arrays.copyOfRange(source, start, end));
        }
        return result;
    }
}
