package hr.fer.zemris.java.hw16.interfaces;

import hr.fer.zemris.java.hw16.jvdraw.model.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.model.Rectangle;

/**
 * Interface that is implemented by a subject that saves instances of {@link GeometricalObject} 
 * class.
 * @author Ante
 *
 */
public interface DrawingModel {

	/**
	 * Number of objects. 
	 * @return number of objects
	 */
	public int getSize();
	
	/**
	 * Returns object at given index.
	 * @param index index
	 * @return object at index
	 */
	public GeometricalObject getObject(int index);
	
	/**
	 * Adds given object. 
	 * @param object object to add
	 */
	public void add(GeometricalObject object);
	
	/**
	 * Adds given listener.
	 * @param l listener to add
	 */
	public void addDrawingModelListener(DrawingModelListener l);
	
	/**
	 * Removes given listener if it exists. 
	 * @param l listener to remove
	 */
	public void removeDrawingModelListener(DrawingModelListener l);
	
	/**
	 * Removes all objects from model. 
	 */
	public void clear();

	/**
	 * Removes object at given index.
	 * @param index index of object
	 */
	public void removeObject(int index);

	/**
	 * Updates values of object at given index.
	 * @param index index
	 */
	public void updateObject(int index);
	
	/**
	 *Returns a minimal bounding box for this model. 
	 * @return minimal bounding box
	 */
	public Rectangle getBoundingBox();
}
