package fullclean.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.SecureRandom;

@Service
public class EncryptionService {

    @Value("${app.encryption.key:}")
    private String base64Key;

    private SecretKeySpec keySpec;

    @PostConstruct
    public void init() {
        if (base64Key == null || base64Key.isBlank()) {
            // No key configured — encryption disabled until key provided
            keySpec = null;
            return;
        }
        byte[] key = Base64.getDecoder().decode(base64Key);
        keySpec = new SecretKeySpec(key, "AES");
    }

    public String encrypt(String plain) {
        if (keySpec == null) {
            throw new IllegalStateException("Encryption key not configured");
        }
        try {
            byte[] iv = new byte[12];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);
            byte[] encrypted = cipher.doFinal(plain.getBytes());
            byte[] out = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, out, 0, iv.length);
            System.arraycopy(encrypted, 0, out, iv.length, encrypted.length);
            return Base64.getEncoder().encodeToString(out);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decrypt(String cipherText) {
        if (keySpec == null) {
            throw new IllegalStateException("Encryption key not configured");
        }
        try {
            byte[] all = Base64.getDecoder().decode(cipherText);
            byte[] iv = new byte[12];
            System.arraycopy(all, 0, iv, 0, iv.length);
            byte[] encrypted = new byte[all.length - iv.length];
            System.arraycopy(all, iv.length, encrypted, 0, encrypted.length);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);
            byte[] plain = cipher.doFinal(encrypted);
            return new String(plain);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
