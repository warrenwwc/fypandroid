package is.fyp.functions;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static org.ow2.util.base64.Base64.decode;
import static org.ow2.util.base64.Base64.encode;

public class Encryptor {
    public static String encrypt(String key, String initVector, String value) {
        initVector += "0000000000000000";
        key += "0000000000000000";
        initVector = initVector.substring(0, 16);
        key = key.substring(0, 16);
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: "
                    + String.valueOf(encode(encrypted)));

            return String.valueOf(encode(encrypted));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String key, String initVector, String encrypted) {
        initVector += "0000000000000000";
        key += "0000000000000000";
        initVector = initVector.substring(0, 16);
        key = key.substring(0, 16);
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(decode(encrypted.toCharArray()));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

//    public static void main(String[] args) {
//        String key = "Bar12345Bar12345"; // 128 bit key
//        String initVector = "RandomInitVector"; // 16 bytes IV
//
//        System.out.println(decrypt(key, initVector,
//                encrypt(key, initVector, "Hello World")));
//    }
}
