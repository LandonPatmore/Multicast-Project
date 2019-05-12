package com.csc445.shared.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AES {

	public static final String TEST_PASSWORD = "123The Legend 69";

    /**
     * Encrypts <code>plainText</code> with <code>secretKey</code>
     * using AES.
     *
     *
     *
     * @param plainText the byte array to be encrypted
     * @param secretKey the key to encrypt <code>plainText</code> with
     * @return the byte array <code>ciphertext</code>
     */
    public static byte[] encryptByteArray( byte[] plainText , String secretKey) throws InvalidKeyException {
        Cipher cipher;
        SecretKeySpec secret;
        byte[] cipherText = null;
        try {
            cipher = Cipher.getInstance("AES");
            secret = new SecretKeySpec(secretKey.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            // TODO: make sure cipherText stays below 500 bytes
            cipherText = cipher.doFinal(plainText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException 	|
				BadPaddingException | IllegalBlockSizeException e) {
			// ignored
		}

		return cipherText;
    }

    /**
     * Decrypts <code>ciphertext</code>, the byte array of <code>plaintext</code>
     * encrypted in AES using <code>secretKey</code>
     *
     *
     *
     * @param cipherText the byte array to decrypt
     * @param secretKey the key to decrypt <code>cipherText</code> with
     * @return the byte array <code>plainText</code>
     */
    public static byte[] decryptByteArray( byte[] cipherText , String secretKey) throws InvalidKeyException {
        Cipher cipher;
        SecretKeySpec secret;
        byte[] plainText = null;
        try {
            cipher = Cipher.getInstance("AES");
            secret = new SecretKeySpec(secretKey.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            plainText = cipher.doFinal(cipherText);
        }  catch (NoSuchAlgorithmException | NoSuchPaddingException 	|
				BadPaddingException | IllegalBlockSizeException e) {
			// ignored
		}

        return plainText;
    }
}
