package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Represents a general collection of objects. This class
 * serves only as an interface for others classes that inherit
 * this class and doesn't provide implementation of it's methods.
 * @author Ante
 *
 */
public class Collection {
	
	/**
	 * Default constructor for this class. 
	 */
	protected Collection() {
	}
	
	/**
	 * Checks if this collections is empty.
	 * .
	 * @return true if this collection is empty, false otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * Returns number of elements in this collection. 
	 * 
	 */
	int size() {
		return 0;
	}
	
	/**
	 * Adds the specified element to this collection. 
	 * @param value element to be added
	 */
	public void add(Object value) {	
	}
	
	/**
	 * Checks if the specified element is in this collection. 
	 * @param value element to be checked for 
	 * @return true if this collection contains specified element,
	 * false otherwise
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	/**
	 * Removes the specified element from this collection. 
	 * @param value element to be removed
	 * @return true if element was found and removed, false otherwise
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * Returns this collection in a form of an array.
	 * @return this collection as an array
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Calls <code>process()</code> method from {@link Processor}
	 * for every element in this collection. 
	 * @param processor processor whose method <code>process()</code> is called
	 */
	public void forEach(Processor processor) {		
	}
	
	/**
	 * Adds all elements from specified collection to this collection. 
	 * @param other collection whose elements are to be placed in this collection
	 * @throws IllegalArgumentException if given collection is null
	 */
	public void addAll(Collection other) {
		if(other == null) {
			throw new IllegalArgumentException();
		}
		
		/**
		 * @{@inheritDoc}
		 *
		 */
		class LocalProcessor extends Processor {
			public void process(Object value) {
				add(value);
			}
		}
		
		other.forEach(new LocalProcessor());
	}
	
	/**
	 * Removes all elements from this collection. 
	 */
	public void clear() {	
	}

}
