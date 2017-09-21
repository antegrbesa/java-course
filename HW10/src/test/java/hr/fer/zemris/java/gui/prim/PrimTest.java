package hr.fer.zemris.java.gui.prim;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrimTest {

	PrimListModel model = new PrimListModel();
	
	@Test
	public void testNext() {
		model.next();
		int number = model.getElementAt(1);
		assertEquals(2, number);
		model.next();
		int number2 = model.getElementAt(2);
		assertEquals(3, number2);
		model.next();
		int number3 = model.getElementAt(3);
		assertEquals(5, number3);
	}

}
