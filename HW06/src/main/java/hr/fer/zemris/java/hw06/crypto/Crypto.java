package hr.fer.zemris.java.hw06.crypto;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A simple program that offers the user to encrypt/decrypt given file using the AES crypto-
 * algorithm and the 128-bit encryption key or calculate and check the SHA-256 file digest.
 * Arguments must be given in command line. Keywords are: {@code checksha}, {@code encrypt} and
 * {@code decrypt}. {@code checksha} expects a single file name, and {@code encrypt} or {@code decrypt}
 * expect source file name and destination file name. 
 * @author Ante
 *
 */
public class Crypto {

	/**Default buffer size*/
	private static final int BUFFER_SIZE = 4096;
	
	/**
	 * This method runs once the program has started. 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		if (args.length < 2 || args.length > 3) {
			System.out.println("Invalid number of command line arguments"
					+ ", terminating program");
			System.exit(1);
		}
		
		Scanner sc = new Scanner(System.in);
		
		if (args[0].equals("checksha") && args.length == 2) {
			checkSHA(sc, args[1]);
		} else if (args[0].equals("encrypt") && args.length == 3) {
			encryptDecryptFile(sc, args[1], args[2], true);
		} else if (args[0].equals("decrypt") && args.length == 3) {
			encryptDecryptFile(sc, args[1], args[2], false);
		} else {
			System.out.println("Invalid arguments in command line");
			System.exit(1);
		}
	}

	/**
	 * Encrypts or decrypts file with specified file name using AES algorithm and 
	 * 128-bit encryption. 
	 * @param sc scanner for console arguments
	 * @param document name of file for encryption/decryption
	 * @param destinationDoc name of destination file 
	 * @param encrypt represents mode, true for encryption mode and false
	 * for decryption mode
	 */
	private static void encryptDecryptFile(Scanner sc, String document, String destinationDoc, boolean encrypt) {
		String keyText = getText(sc, "password", "(16 bytes, i.e. 32 hex-digits)");
		String ivText =  getText(sc, "initialization vector", "(32 hex-digits)");
		
		Cipher cipher = generateCipher(keyText, ivText, encrypt);
		
		Path pathRead = Paths.get(document);
		Path pathWrite = Paths.get(destinationDoc);
		try (
				InputStream is = new BufferedInputStream(new FileInputStream(pathRead.toFile()));
				OutputStream os = new BufferedOutputStream(new FileOutputStream(pathWrite.toFile()))
			) {
			byte[] buffer = new byte[BUFFER_SIZE];
			while(true) {
				int n = is.read(buffer);
				if (n < 1) {
					break;
				}
				os.write(cipher.update(buffer, 0, n));
			}
			
			os.write(cipher.doFinal());
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return;
		} catch (BadPaddingException | IllegalBlockSizeException e) {
			System.out.println(e.getMessage());
			return;
		}
		String s = encrypt ? "Encryption" : "Decryption";
		System.out.println(s+" completed. Generated file"
				+destinationDoc+" based on file "+document);
	}

	/**
	 * Generates an instance of {@link Cipher} class with specified arguments.}
	 * @param keyText user-provided password
	 * @param ivText  user provided  initialization vector 
	 * @param encrypt operation mode, true for encryption and false for decryption
	 * @return newly created instance of {@link Cipher} class
	 */
	private static Cipher generateCipher(String keyText, String ivText, boolean encrypt) {
		SecretKeySpec keySpec = new SecretKeySpec(Util.hexToByte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hexToByte(ivText));
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			return null;
		}
		
		try {
			cipher.init(encrypt ? Cipher.ENCRYPT_MODE: Cipher.DECRYPT_MODE, keySpec, paramSpec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			return null;
		}
		
		return cipher;
	}
	
	/**
	 * Gets string argument from console
	 * @param sc input scanner
	 * @param value name of wanted value
	 * @param description description of value
	 * @return next line from console as string
	 */
	private static String getText(Scanner sc, String value, String description) {
		System.out.println("Please provide "+value+" as hex-encoded text "+description+" :");
		System.out.print("> ");
		
		return sc.nextLine();
	}
	
	/**
	 * Calculates SHA-256 file digest for specified document. 
	 * @param sc console input scanner
	 * @param document document to calculate digest from
	 */
	private static void checkSHA(Scanner sc, String document) {
		System.out.println("Please provide expected SHA-256 digest for "+document+" :");
		System.out.print("> ");
		String digest = sc.next();
		
		if (digest.length() != 64) {
			System.out.println("Invalid digest length.");
			return;
		}
		
		byte[] generatedDigest = getDigest(document);
		
		String newDigest = Util.byteToHex(generatedDigest);
		if (newDigest.equals(digest)) {
			System.out.println("Digesting completed. Digest of "+document
					+" matches expected digest.");
		} else {
			System.out.println("Digesting completed. Digest of "+document+" does not match the"
					+ " expected digest. Digest was: "+newDigest);
		}
	}
	
	/**
	 * Returns SHA-256 digest for specified document.
	 * @param document document to calculate digest from
	 * @return generated digest 
	 */
	private static byte[] getDigest(String document) {
		Path p = Paths.get(".\\"+document);
		
		byte[] generatedDigest = null;
		try (InputStream is = new FileInputStream(p.toFile())) {
			MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
			byte[] buffer = new byte[BUFFER_SIZE];
			
			while (true) {
				int n = is.read(buffer);
				if( n < 1) {
					break;
				}
				
				msgDigest.update(buffer, 0, n);
			}
			
			generatedDigest = msgDigest.digest();
		} catch (IOException e) {
			System.out.println("File not found, terminating program.");
			return null;
		} catch (NoSuchAlgorithmException e2) {
			System.out.println("No such algorithm");
			return null;
		}
		
		return generatedDigest;		
	}
	
}
