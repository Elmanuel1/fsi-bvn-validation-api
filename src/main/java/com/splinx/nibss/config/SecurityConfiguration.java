package com.splinx.nibss.config;

import com.splinx.nibss.exceptions.EncryptionException;
import com.splinx.nibss.util.StringUtils;
import com.splinx.nibss.vo.ResetConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class SecurityConfiguration {

    @Autowired
    private ConfigurationSingleton configurationSingleton;
    private SecretKeySpec encryptionKey;
    private IvParameterSpec ivKey;
    private static Cipher cipherEncrypt, cipherDecrypt;

    private ResetConfiguration resetConfiguration;
    public static Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);

    private void setEncryptionKeys() throws EncryptionException {

        String encryptionKey = resetConfiguration.getAesKey();
        String ivKey = resetConfiguration.getIvKey();
        if (!StringUtils.isBlank(encryptionKey) && !StringUtils.isBlank(ivKey)) {
            try {
                this.encryptionKey = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
                this.ivKey = new IvParameterSpec(ivKey.getBytes("UTF-8"));

                cipherEncrypt = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipherEncrypt.init(Cipher.ENCRYPT_MODE, this.encryptionKey, this.ivKey);

                cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipherDecrypt.init(Cipher.DECRYPT_MODE, this.encryptionKey, this.ivKey);
                return;
            } catch ( UnsupportedEncodingException | NoSuchPaddingException | NoSuchAlgorithmException|InvalidAlgorithmParameterException | InvalidKeyException e) {
                throw new EncryptionException(configurationSingleton.getErrorSettingEncryptionKeys(), "Error occurred setting encryption details" , e);

            }
        }

        throw new EncryptionException(configurationSingleton.getEmptyKeyIvErrorCode(), "Either of ivkey or encryption key is not provided.");
    }


    public  String encrypt(String stringToEncrypt) throws EncryptionException {
        if (!StringUtils.isBlank(stringToEncrypt)) {
            try {
                byte[] encrypted = cipherEncrypt.doFinal(stringToEncrypt.getBytes());
                return StringUtils.bytesToHex(encrypted);
            } catch ( IllegalBlockSizeException | BadPaddingException e) {
                throw new EncryptionException(configurationSingleton.getEncryptionErrorCode(), "Error occurred encrypting details" , e);

            }
        }

        throw new EncryptionException(configurationSingleton.getEmptyValueEncryptErrorCode(), "Error occurred encrypting. Trying to encrypt null value");
    }

    public String decrypt(String encrypted) throws EncryptionException {
        if (!StringUtils.isBlank(encrypted)) {
            try {
                byte[] original = cipherDecrypt.doFinal(StringUtils.hexStringToByteArray(encrypted));
                return new String(original);
            } catch ( IllegalBlockSizeException | BadPaddingException e) {
                throw new EncryptionException(configurationSingleton.getDecryptionErrorCode(), "Error occurred decrypting details" , e);
            }
        }
        throw new EncryptionException(configurationSingleton.getEmptyValueEncryptErrorCode(), "Error occurred encrypting. Trying to encrypt null value");
    }

    public byte[] getSHA(String input) throws EncryptionException {

        if (!StringUtils.isBlank(input)) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                return md.digest(input.getBytes());
            } catch ( NoSuchAlgorithmException e) {
                throw new EncryptionException(configurationSingleton.getEncryptionWithoutIvErrorCode(), "Error occurred encryption input" , e);
            }
        }
        throw new EncryptionException(configurationSingleton.getEmptyValueEncryptErrorCode(), "Error occurred getting sha256 encryption. Trying to encrypt null or empty value");



    }

    public ResetConfiguration getResetConfiguration() {
        return resetConfiguration;
    }

    public void setResetConfiguration(ResetConfiguration resetConfiguration) throws EncryptionException {

        this.resetConfiguration = resetConfiguration;
        setEncryptionKeys();
    }
}