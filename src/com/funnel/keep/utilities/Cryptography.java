package com.funnel.keep.utilities;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.util.Log;

public class Cryptography {

	public static String decrypt(SecretKeySpec sks, String text) {
		byte[] encodedBytes = Base64.decode(text.getBytes(), 0);
		byte[] decodedBytes = null;
		try {
			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.DECRYPT_MODE, sks);
			decodedBytes = c.doFinal(encodedBytes);
		} catch (Exception e) {
			Log.e("", "AES decryption error");
		}
		return new String(decodedBytes);
	}

	public static String encrypt(String test, SecretKeySpec sks) {

		byte[] encodedBytes = null;
		try {
			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.ENCRYPT_MODE, sks);
			encodedBytes = c.doFinal(test.getBytes());
		} catch (Exception e) {
			Log.e("", "AES encryption error");
		}
		String resulst = Base64.encodeToString(encodedBytes, Base64.DEFAULT);

		return resulst;

	}

	public static String hashMD5(String password) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			byte[] array = md.digest((password + "!@#$%^&*(").getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString().toUpperCase(Locale.getDefault());
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;

	}

	public static SecretKeySpec getMySecretKey128Bits(String stringKey) {

		byte[] key = null;
		try {
			key = ("!@#$%^&*()" + stringKey).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16); // use only first 128 bit

		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

		// byte[] encodedKey = Base64.decode(stringKey, Base64.DEFAULT);
		// SecretKeySpec originalKey = new SecretKeySpec(encodedKey, 0,
		// encodedKey.length, "AES");
		return secretKeySpec;

	}

	public static SecretKeySpec generateSecretKeySpecBasedOnASeed(String seed) {
		SecretKeySpec sks = null;
		try {
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.setSeed(seed.getBytes());
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			kg.init(128, sr);
			sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
		} catch (Exception e) {
			Log.e("", "AES secret key spec error");
		}
		return sks;

	}

}
