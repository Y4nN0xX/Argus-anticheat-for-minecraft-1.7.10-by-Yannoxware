package fr.yannox.feurgus.keys;

import java.nio.charset.StandardCharsets;

public class StaticKey {
    public static String q = "2r5u8x/A?D*G-KaP";

    public static byte[] a() throws Exception {
        return q.getBytes(StandardCharsets.UTF_8);
    }
}
