package fr.yannox.feurgus.keys;

import fr.yannox.feurgus.keys.DynamicKeyGetter;
import fr.yannox.feurgus.utils.garbage.Garbage2;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class KeyUtils {
    public static byte[] encrypt(byte[] array) {
        byte[] encodedData = Base64.getEncoder().encode(array);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            DeflaterOutputStream dos = new DeflaterOutputStream(baos);
            dos.write(encodedData);
            dos.flush();
            dos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        byte[] encodedFirstKeyData = null;
        try {
            encodedFirstKeyData = KeyUtils.encrypt(DynamicKeyGetter.IiIIiI(), baos.toByteArray());
        }
        catch (Exception error) {
            error.printStackTrace();
            return null;
        }
        return KeyUtils.encrypt(Garbage2.iIiiIiiIii, encodedFirstKeyData);
    }

    private static byte[] encrypt(byte[] key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(1, (Key)secretKey, ivparameterspec);
            return cipher.doFinal(data);
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static byte[] decrypt(byte[] key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(2, (Key)secretKey, ivparameterspec);
            return cipher.doFinal(data);
        }
        catch (Exception exception) {
            return null;
        }
    }
}
