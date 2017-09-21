package hr.fer.zemris.java.hw05.observer1;

/**
 * Implementation of a listener that writes to the standard output double value (i.e. “value * 2”) 
 * of the current value which is stored in subject, but only first n times since its registration with the subject 
 * (n is given in constructor).
 * @author Ante
 *
 */
public class DoubleValue implements IntegerStorageObserver {
	
	/**Number of iterations*/
	private int n;
	
	
	/**
	 * Constructor for this class.
	 * @param n number of times to perfrom operation
	 */
	public DoubleValue(int n) {
		super();
		this.n = n;
	}



	@Override
	public void valueChanged(IntegerStorage istorage) {
		if(n == 0) {
			return;
		}
		
		System.out.println("Double value: "+istorage.getValue()*2);
		n--;
		if(n == 0) {
			istorage.removeObserver(this);
		}
	}

}
