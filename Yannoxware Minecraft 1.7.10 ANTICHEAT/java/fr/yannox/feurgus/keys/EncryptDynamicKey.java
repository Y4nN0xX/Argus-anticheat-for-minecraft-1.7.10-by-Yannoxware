package fr.yannox.feurgus.keys;

import fr.yannox.feurgus.keys.DynamicKey;
import fr.yannox.feurgus.keys.StaticKey;
import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDynamicKey {
    public static String encrypt() {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec secretKey = new SecretKeySpec(StaticKey.a(), "AES");
            IvParameterSpec ivparameterspec = new IvParameterSpec(StaticKey.a());
            cipher.init(1, (Key)secretKey, ivparameterspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(DynamicKey.IiIIIIiIiiI.getBytes()));
        }
        catch (Exception exception) {
            return null;
        }
    }
}
