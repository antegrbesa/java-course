package hr.fer.zemris.java.hw05.observer1;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a <i>Subject</i> class that holds an integer value and provides API for
 * registering listeners to this value. Listeners must implement {@link IntegerStorageObserver}.
 * @author Ante Grbesa
 *
 */
public class IntegerStorage {

	/**Holding value*/
	private int value;
	
	/**List of observers*/
	private List<IntegerStorageObserver> observers;	
	
	/**
	 * Creates instance of this class that stores specified value.
	 * @param initialValue value to store
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
		observers = new ArrayList<>();
 	}
	
	/**
	 * Adds an listener to this class.
	 * @param observer listener to register
	 */
	public void addObserver(IntegerStorageObserver observer) {
		observers = new ArrayList<>(observers);
		if(observers.contains(observer)) {
			return;
		}
		
		observers.add(observer);
	}
	
	/**
	 * Removes given observer from this class
	 * @param observer observer to remove
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		observers = new ArrayList<>(observers);
		if(observers.contains(observer)) {
			observers.remove(observer);
		}
	}
	
	/**
	 * Removes all observers from this class.
	 */
	public void clearObservers() {
		observers.clear();
	}
	
	/**
	 * Returns stored value.
	 * @return stored value
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Sets the current value to the specified one and notifies
	 * all observers.
	 * @param value new value to set
	 */
	public void setValue(int value) {
		if(this.value != value) {
			this.value = value;
			if(observers != null) {
				for(IntegerStorageObserver obs : observers) {
					obs.valueChanged(this);
				}
			}
		}
	}
	
}
