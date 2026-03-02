import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class TestPasswords {
    public static void main(String[] args) {
        String[] commonPasswords = { "argusadmin", "password", "admin", "Admin@123", "123456", "argus@123" };
        String key = "argusadmin123456";
        String ivString = "argusadmin123456";

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec iv = new IvParameterSpec(ivString.getBytes(StandardCharsets.UTF_8));

            for (String pw : commonPasswords) {
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
                byte[] encrypted = cipher.doFinal(pw.getBytes(StandardCharsets.UTF_8));
                String encStr = Base64.getEncoder().encodeToString(encrypted);
                System.out.println("Password: " + pw + " -> " + encStr);
            }

            String target = "2JpZ8jdGmQ0uln1aaUpZGTN8x3Ixqg8C";
            System.out.println("Target: " + target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
