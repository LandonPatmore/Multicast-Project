package com.csc445.shared.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Arrays;

public class AES {

    public static final String TEST_PASSWORD = "123The Legend 69";

    /**
     * <p>Encrypts <code>plainText</code> with <code>secretKey</code>
     * using AES.</p>
     *
     * <p>Data is encrypted using AES in CBC mode, with PKCS5 padding.</p>
     *
     * <p>CBC mode requires an initialization vector (<code>iv</code>)
     * which must be different if the same <code>secretKey</code> is reused.
     * This method generated a new <code>iv</code> every time it is called,
     * regardless of the <code>secretKey</code>.</p>
     *
     * <p>Size of <code>cipherText</code> is calculated as follows:<br/>
     * <code>cipherText.length == ((plainText.length / 16) + 1) * 16</code>
     * </p>
     *
     * @param plainText the byte array to be encrypted
     * @param secretKey the key to encrypt <code>plainText</code> with
     * @return the byte array <code>ciphertext</code>, null if cannot be decrypted
     */
    public static byte[] encryptByteArray(byte[] plainText, int plainTextLength, String secretKey) throws InvalidKeyException {
        try {
            final byte[] trimmedPlainText = Arrays.copyOfRange(plainText, 0, plainTextLength);
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes(), "AES");

            // IV: initialization vector.
            // IV does not need to be secret
            // encode with Base64, prepend to ciphertext and transmit
            // FRESH IV MUST ALWAYS BE GENERATED BEFORE ENCRYPTION
            final byte[] iv = SecureRandom.getSeed(16);

            cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(iv));

            byte[] encrypted = cipher.doFinal(trimmedPlainText);
            final byte[] cipherText = new byte[encrypted.length + iv.length];

            for (int i = 0; i < cipherText.length; ++i) {
                if (i < iv.length) {
                    cipherText[i] = iv[i];
                } else {
                    cipherText[i] = encrypted[i - iv.length];
                }
            }

            return cipherText;
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException ignored) {
            return null;
        }
    }

    /**
     * Decrypts <code>ciphertext</code>, the byte array of <code>plaintext</code>
     * encrypted in AES using <code>secretKey</code>
     *
     * @param cipherText the byte array to decrypt
     * @param secretKey  the key to decrypt <code>cipherText</code> with
     * @return the byte array <code>plainText</code>, null if cannot be decrypted
     */
    public static byte[] decryptByteArray(byte[] cipherText, int cipherTextLength, String secretKey) throws InvalidKeyException {
        try {
            final byte[] trimmedPlainText = Arrays.copyOfRange(cipherText, 0, cipherTextLength);
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes(), "AES");

            // extract and decode iv from payload
            byte[] iv = Arrays.copyOfRange(trimmedPlainText, 0, 16);

            cipher.init(Cipher.DECRYPT_MODE, secret,
                    new IvParameterSpec(iv));

            return cipher.doFinal(Arrays.copyOfRange(trimmedPlainText, iv.length, trimmedPlainText.length));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException ignored) {
            return null;
        }
    }
}
