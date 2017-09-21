package hr.fer.zemris.hw15.util;


/**
 * This class offers two public static methods, {@link #hexToByte(String)} and 
 * {@link #byteToHex(byte[])} designed for converting hex string to byte array and
 * vice versa.
 * @author Ante Grbesa
 *
 */
public class Util {

	/**
	 * Takes hex-encoded String as argument and returns appropriate {@code byte[]}. Method supports 
	 * both upper-case and lower-case letters in argument. 
	 * @param keyText hex-encoded String
	 * @return  returns appropriate {@code byte[]} generated from argument String
	 * @throws IllegalArgumentException if specified String is odd-sized or has invalid
	 * characters.
	 */
	public static byte[] hexToByte(String keyText) {
		int length = keyText.length();
		if(length % 2 != 0) {
			throw new IllegalArgumentException("Hex input needs to be even length.");
		}
		
		byte[] returnArray = new byte[length/2];
		
		for(int i = 0; i < length; i+=2) {
			int j = parseHexChar(keyText.charAt(i));
			int k = parseHexChar(keyText.charAt(i+1));
			
			returnArray[i/2] = (byte) (j * 16 + k);
		}
		
		return returnArray;
	}
	
	/**
	 * Returns integer value of a given character represented as hex-character.
	 * @param ch hex-character
	 * @return integer value of specified character interpreted as hex-digit
	 * @throws IllegalArgumentException if given character is not valid hexadecimal
	 * digit
	 */
	private static int parseHexChar(char ch) {
		if (ch >= '0' && ch <= '9') {
            return ch - '0';
        }
        if (ch >= 'A' && ch <= 'F') {
            return ch - 'A' + 10;
        }
        if (ch >= 'a' && ch <= 'f') {
            return ch - 'a' + 10;
        }
        
       throw new IllegalArgumentException("Illegal character for hex value, was "+ch);
	}
	
	/**
	 * Takes {@code byte[]} and creates it's hex-encoding value
	 * as String.  For each byte of given array, two characters are returned in String
	 *  in <i>big endian</i> notation.
	 * @param byteArray array to encode
	 * @return arguments'  hex-encoding value as String
	 */
	public static String byteToHex(byte[] byteArray) {
		StringBuilder sb = new StringBuilder(byteArray.length * 2);
		char[] hexDigits = "0123456789abcdef".toCharArray();
		
		for(byte b : byteArray) {
			sb.append((hexDigits[(b>>4) & 0x0F]));	//upper 4 bits shifted to lower 4 bits as single character
			sb.append(hexDigits[b & 0x0F]);	//lower 4 bits as single character
		}
		
		return sb.toString();
		
	}
}
