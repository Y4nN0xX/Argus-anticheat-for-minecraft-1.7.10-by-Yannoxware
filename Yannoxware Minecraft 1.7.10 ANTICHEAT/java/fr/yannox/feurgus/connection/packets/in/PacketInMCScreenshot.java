package fr.yannox.feurgus.connection.packets.in;

import fr.yannox.feurgus.checks.minecraft.CheckMCScreenshot;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.in.PacketIn;
import fr.yannox.feurgus.connection.packets.out.PacketOutMCScreenshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketInMCScreenshot
extends PacketIn {
    @Override
    public int getPacketId() {
        return 1;
    }

    @Override
    public void fromBytes(byte[] bytes) {
        CheckMCScreenshot chk = new CheckMCScreenshot();
        chk.getScreenshot(data -> {
            List<byte[]> array = this.divideArray(data, 4096);
            int size = array.size();
            for (int i = 0; i < size; ++i) {
                if (InternalSession.instance == null) {
                    return;
                }
                PacketOutMCScreenshot pkt = new PacketOutMCScreenshot(i, size, array.get(i));
                try {
                    InternalSession.instance.sendPacket(pkt);
                    continue;
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        });
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
