package hr.fer.zemris.java.hw05.observer1;

/**
 * Implementation of a listener that  counts (and writes to the standard output) the number of times the 
 * value stored has been changed since the registration.
 * @author Ante Grbesa
 *
 */
public class ChangeCounter implements IntegerStorageObserver {
	
	/**Counter for changes*/
	private int i = 0;
	
	@Override
	public void valueChanged(IntegerStorage istorage) {
		i++;
		System.out.println("Number of value changes since tracking: "+i);
	}

}
