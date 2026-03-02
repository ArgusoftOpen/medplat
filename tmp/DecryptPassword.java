import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class DecryptPassword {
    public static void main(String[] args) {
        try {
            String key = "argusadmin123456";
            String ivString = "argusadmin123456";
            String encrypted = "2JpZ8jdGmQ0uln1aaUpZGTN8x3Ixqg8C";

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec iv = new IvParameterSpec(ivString.getBytes(StandardCharsets.UTF_8));

            // Test Encryption
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] testEncrypted = cipher.doFinal("argusadmin".getBytes(StandardCharsets.UTF_8));
            String testEncryptedStr = Base64.getEncoder().encodeToString(testEncrypted);
            System.out.println("Encrypted 'argusadmin': " + testEncryptedStr);

            // Decrypt the target
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            System.out.println("Decrypted Password: " + new String(original, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
