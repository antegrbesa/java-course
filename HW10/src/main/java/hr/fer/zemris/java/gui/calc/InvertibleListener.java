package hr.fer.zemris.java.gui.calc;

/**
 * Models an invertible listener that contains two operations. 
 * @author Ante Grbesa
 *
 */
public interface InvertibleListener {

	/**
	 * Inverts this listener. 
	 */
	public void invert();
	
	/**
	 * Returns the name of listener
	 * @return name of listener
	 */
	public String getName();
}
