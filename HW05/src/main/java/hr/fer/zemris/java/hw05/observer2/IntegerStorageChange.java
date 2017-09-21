package hr.fer.zemris.java.hw05.observer2;

public class IntegerStorageChange {

	private IntegerStorage storage;
	
	private int oldValue;
	
	private int currentValue;

	public IntegerStorageChange(IntegerStorage storage, int oldValue, int currentValue) {
		super();
		this.storage = storage;
		this.oldValue = oldValue;
		this.currentValue = currentValue;
	}
	
	public IntegerStorage getStorage() {
		return storage;
	}
	
	public int getOldValue() {
		return oldValue;
	}
	
	public int getCurrentValue() {
		return currentValue;
	}
	
	
}
