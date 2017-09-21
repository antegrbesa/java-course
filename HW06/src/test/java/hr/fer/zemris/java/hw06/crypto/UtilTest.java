package hr.fer.zemris.java.hw06.crypto;

import static org.junit.Assert.*;

import org.junit.Test;


@SuppressWarnings("javadoc")
public class UtilTest {

	@Test
	public void testHexToByte() {
		byte[] b = new byte[] {1, -82, 34};
		assertArrayEquals(b, Util.hexToByte("01aE22"));
	}

	@Test
	public void testByteToHex() {
		byte[] b = new byte[] {1, -82, 34};
		assertEquals("01ae22", Util.byteToHex(b));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testHexToByteOdd() {
		byte[] b = new byte[] {1, -82, 34};
		assertArrayEquals(b, Util.hexToByte("01aE220"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testHexToByteInvalid() {
		byte[] b = new byte[] {1, -82, 34};
		assertArrayEquals(b, Util.hexToByte("01aG220"));
	}
	

	public void testHexToByteEmptyString() {
		byte[] b = new byte[0];
		assertArrayEquals(b, Util.hexToByte(""));
	}
	
	@Test
	public void testByteToHexEmpty() {
		byte[] b = new byte[0];
		assertEquals("", Util.byteToHex(b));
	}
	
	
	
}
