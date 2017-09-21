package hr.fer.zemris.java.hw05.observer2;


/**
 * Demonstration class for {@link IntegerStorage} class.
 * @author Ante
 *
 */
public class ObserverExample {

	/**
	 * This method runs once the program starts.
	 * @param args not used
	 */
	public static void main(String[] args) {
		
		IntegerStorage istorage = new IntegerStorage(20);
		IntegerStorageObserver observer = new SquareValue();
		istorage.addObserver(observer);
		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(2));
		
		
		istorage.setValue(5);
		istorage.setValue(2);
		istorage.setValue(25);
		istorage.setValue(13);
		istorage.setValue(22);
		istorage.setValue(15);	
	}

}
