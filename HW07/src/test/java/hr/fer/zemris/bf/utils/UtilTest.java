package hr.fer.zemris.bf.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilTest {

	@Test
	public void testBooleanArrayToInt() {
		boolean[] values1 = {false, false, false, true};
		boolean[] values2 = {false, false, true, false};
		boolean[] values3 = {false, false, true, true};
		
		assertEquals(1, Util.booleanArrayToInt(values1));
		assertEquals(2, Util.booleanArrayToInt(values2));
		assertEquals(3, Util.booleanArrayToInt(values3));
	}

}
