package hr.fer.zemris.hw15.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class that offers method to encrypt a String. 
 * @author Ante
 *
 */
public class CryptoUtil {

	/**
	 * Returns SHA-256 digest for specified password.
	 * @param password password to calculate digest from
	 * @return generated digest 
	 * @throws NoSuchAlgorithmException -
	 * @throws UnsupportedEncodingException -
	 */
	public static String getDigest(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest msgDigest = MessageDigest.getInstance("SHA-1");
		msgDigest.update(password.getBytes());
		
		byte [] generatedDigest = msgDigest.digest();
	
		return Util.byteToHex(generatedDigest);		
	}
}
