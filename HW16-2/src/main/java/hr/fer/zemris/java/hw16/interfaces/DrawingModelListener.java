package hr.fer.zemris.java.hw16.interfaces;

/**
 * Interface that all listeners for {@link DrawingModel} class must implement. 
 * @author Ante
 *
 */
public interface DrawingModelListener {

	/**
	 * Called when objects are added. 
	 * @param source drawing model
	 * @param index0 start index
	 * @param index1 end index
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);
	
	/**
	 * Called when objects are removed.
	 * @param source drawing model
	 * @param index0 start index
	 * @param index1 end index
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);
	
	/**
	 * Called when an object has changed.
	 * @param source drawing model
	 * @param index0 start index
	 * @param index1 end index
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);
}
