package Group4.StudyHubBackendG4.services;

import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordService {

    private static PasswordService instance = null;
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String SECRET = "emrYoruZKktTVfw5";

    public PasswordService() {
    }

    public static PasswordService getInstance() {
        if (instance == null) {
            instance = new PasswordService();
        }
        return instance;
    }

    public String hashPassword(String password){
        if(null == password){
            return null;
        }
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public Boolean checkPasswordHash(String candidate, String hash){
        return BCrypt.checkpw(candidate, hash);
    }

    public String getRandomPassword(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    private String encryptPassword(String password, String key){
            try {
                SecretKeySpec skeyspec = new SecretKeySpec(key.getBytes(), "Blowfish");
                Cipher cipher = Cipher.getInstance("Blowfish");
                cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
                byte[] encrypted = cipher.doFinal(password.getBytes());
                return new String(Base64.getEncoder().encodeToString(encrypted));
            }
            catch (Exception e) {
                return null;
            }
    }

    private String decryptPassword(String encryptedPassword, String key){
            try {
                SecretKeySpec skeyspec = new SecretKeySpec(key.getBytes(), "Blowfish");
                Cipher cipher = Cipher.getInstance("Blowfish");
                cipher.init(Cipher.DECRYPT_MODE, skeyspec);
                byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
                return new String(decrypted);
            }
            catch (Exception e) {
                return null;
            }
    }

    public String encryptPassword(String password){
        return encryptPassword(password, SECRET);
    }

    public String decryptPassword(String encryptedPassword){
        return decryptPassword(encryptedPassword, SECRET);
    }


}
