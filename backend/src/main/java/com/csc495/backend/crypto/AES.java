package com.csc495.backend.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;

public class AES {


	public static byte[] encryptByteArray( byte[] plainText , String secretKey)  {
		Cipher cipher;
		SecretKeySpec secret;
		byte[] cipherText = null;
		try {
			cipher = Cipher.getInstance("AES");
			secret = new SecretKeySpec(secretKey.getBytes(), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secret);
			cipherText = cipher.doFinal(plainText);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (Exception e){
			// ignored
		}

		return cipherText;
	}

	public static byte[] decryptByteArray( byte[] cipherText , String secretKey)  {
		Cipher cipher;
		SecretKeySpec secret;
		byte[] plainText = null;
		try {
			cipher = Cipher.getInstance("AES");
			secret = new SecretKeySpec(secretKey.getBytes(), "AES");
			cipher.init(Cipher.DECRYPT_MODE, secret);
			plainText = cipher.doFinal(cipherText);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (Exception e){
			// ignored
		}

		return plainText;
	}

}
