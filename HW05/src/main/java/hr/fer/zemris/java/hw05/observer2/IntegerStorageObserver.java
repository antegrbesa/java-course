package hr.fer.zemris.java.hw05.observer2;

import hr.fer.zemris.java.hw05.observer1.IntegerStorage;

/**
 * <i>Observer</i> interface for listeners for {@link IntegerStorage} class.
 * @author Ante Grbesa
 *
 */
public interface IntegerStorageObserver {

	/**
	 * Method for notifying this observer of a change. 
	 * @param istorage reference to class that this observer is registered
	 * to
	 */
	public void valueChanged(IntegerStorageChange istorage);
}
