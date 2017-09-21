package hr.fer.zemris.java.hw05.observer2;

import hr.fer.zemris.java.hw05.observer1.IntegerStorage;

/**
 * Implementation of a listener that  writes a square of the integer stored in the {@link IntegerStorage}
 *  to the standard output
 * @author Ante
 *
 */
public class SquareValue implements IntegerStorageObserver {

	@Override
	public void valueChanged(IntegerStorageChange istorage) {
		int value = istorage.getCurrentValue();
		System.out.printf("Provided new value: %d, square is %d%n", value, value*value);

	}

}
