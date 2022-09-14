package fr.yannox.feurgus.utils;

import com.google.common.hash.Hashing;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;
import java.security.SecureRandom;
import java.util.AbstractMap;
import java.util.Map;

public class NetworkKey {
    private static byte[] keepAliveMapper = new byte[]{84, 76, 79, 70, 6, 44, 2, 63, 15, 46, 21, 1, 36, 17, 39, 81, 41, 18, 49, 8, 27, 83, 57, 75, 56, 61, 0, 28, 45, 45, 77, 46, 13, 14, 56, 12, 73, 82, 55, 78, 33, 74, 48, 11, 61, 58, 14, 6, 46, 38, 30, 42, 7, 72, 7, 35, 49, 77, 50, 50, 78, 19, 50, 38};
    private final byte[] baseKey = this.generateRandomString();
    private byte[] keepAliveKey = this.baseKey;

    private byte[] generateRandomString() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[0x4000000];
        random.nextBytes(bytes);
        return Hashing.sha512().hashBytes(bytes).asBytes();
    }

    public byte[] getBaseKey() {
        return this.baseKey;
    }

    public Map.Entry<Long, byte[]> generateKey() {
        long nanoTime = System.nanoTime();
        MyByteArrayDataOutput output = new MyByteArrayDataOutput();
        output.write(this.baseKey);
        output.writeLong(nanoTime);
        byte[] encodedNanoTime = Hashing.sha512().hashBytes(output.toByteArray()).asBytes();
        return new AbstractMap.SimpleEntry<Long, byte[]>(nanoTime, encodedNanoTime);
    }

    public byte[] getKeepAliveKey() {
        return this.keepAliveKey;
    }

    public void generateKeepAliveKey() {
        for (int i = 0; i < this.baseKey.length; ++i) {
            int rest = i % 4;
            this.keepAliveKey[i] = rest == 0 ? (byte)(this.baseKey[i] ^ keepAliveMapper[i]) : (rest == 1 ? (byte)(this.baseKey[i] >> keepAliveMapper[i]) : (rest == 2 ? (byte)(this.baseKey[i] << keepAliveMapper[i]) : (byte)(this.baseKey[i] & keepAliveMapper[i])));
        }
        this.keepAliveKey = Hashing.sha512().hashBytes(this.baseKey).asBytes();
    }
}
