/**
 * 
 */
package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Arrays;

/**
 * Resizable array-backed implementation of the {@link Collection} class with some additional methods. 
 * Permits all  elements except null. Duplications in this collection are allowed. 
 * Default capacity for this collection is 16.
 *
 * @author Ante Grbesa
 *
 */
public class ArrayIndexedCollection extends Collection {
	/**Current number of elements this collection contains. */
	private int size;
	
	/**Maximum capacity for this collection.*/
	private int capacity;
	
	/**Default initial capacity for this collection*/
	private static final int DEFAULT_CAPACITY = 16;
	
	/**The array into which the elements of this collection are stored.*/
	private Object elements[];
	
	
	/**
	 * Constructs an array containing the elements of the specified collection with the
	 * specified initial capacity if specified capacity is equal to or larger than the size
	 * of specified collection, otherwise sets the capacity to
	 * the size of given collection. If specified collection is null, creates an empty
	 * array with specified initial capacity.
	 * @param other collection whose elements are to be placed in this collection
	 * @param initialCapacity the initial capacity of the list
	 * @throws IllegalArgumentException if specified initial capacity is less than 1
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		if(initialCapacity < 1) {
			throw new IllegalArgumentException();
		}
		
		if(other == null) {
			this.elements = new Object[initialCapacity];
			this.size = 0;
		} else {
			this.size = other.size();
			this.elements = Arrays.copyOf(other.toArray(), initialCapacity);
		}
		this.capacity = initialCapacity;		
	}
	
	/**
	 *  Constructs an array containing the elements of the specified collection with initial 
	 *  capacity set to the size of specified collection.
	 * @param other collection whose elements are to be placed in this collection
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, other.size());
	}
	
	/**
	 * Constructs an empty array with the specified initial capacity.
	 * @param initialCapacity  the initial capacity of the list
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		this(null, initialCapacity);
	}
	
	/**
	 * Constructs an empty array with initial capacity set to 16.
	 */
	public ArrayIndexedCollection() {
		this(null, DEFAULT_CAPACITY);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Adds the specified element to the first empty place in backing array. If the
	 * backing array is full, it is reallocated by doubling it's size. Null values aren't
	 * allowed.  
	 * @param value value to be added 
	 * @throws IllegalArgumentException if given value is null
	 */
	@Override
	public void add(Object value) {
		if(value == null) {
			throw new IllegalArgumentException();
		}
		
		if(size >= capacity) {
			capacity *= 2;
			elements = Arrays.copyOf(elements, capacity);
		}
		
		elements[size++] = value;
	}
	
	/**
	 * Returns the element that is at stored at the given position in backing array.
	 * @param index index of element to retrieve 
	 * @return the element at the specified index
	 * @throws IndexOutOfBoundsException if index < 0 or index > size-1
	 */
	public Object get(int index) {
		if(index < 0 || index > size-1) {
			throw new IndexOutOfBoundsException();
		}
		
		return elements[index];
	}
	
	/**
	 * Removes all elements from this collection. Collection will be empty after this call returns and 
	 * will retain it's current capacity.
	 */
	public void clear() {	
		for(int i = 0; i < size; i++) {
			elements[i] = null;
		}
		size = 0;
	}
	
	/**
	 * Inserts(doesn't overwrite) an element to a desired position by shifting right all elements in backing-array
	 *  after given position. The position must be in range of 0 to size. 
	 * @param value value to be inserted
	 * @param position position to insert given element at
	 * @throws IndexOutOfBoundsException if given position is out of range of 0 to size
	 * @throws IllegalArgumentException if given value is null
	 */
	public void insert(Object value, int position) {
		if(position < 0 || position > size) {
			throw new IndexOutOfBoundsException();
		}
		if(value == null) {
			throw new IllegalArgumentException();
		}	
		
		if(size == capacity) {
			elements = Arrays.copyOf(elements, capacity * 2);
		}
		
		for(int i = size; i >= position; i--) {
			if(i == position) {
				elements[position] = value;
				break;
			}
			
			elements[i] = elements[i-1];
		}
		
		size++;
	}
	
	/**
	 * Returns the index of the first occurrence of the specified element. If the element 
	 * is not found, returns -1.
	 * @param value element of which the index value is to be found 
	 * @return index value of the specified element if element is present in this collection,
	 * -1 otherwise
	 */
	public int indexOf(Object value) {
		int index = 0;
		
		for(Object tmp : elements) {
			if(tmp == null) {
				break;
			}
			
			if(tmp.equals(value)) {
				return index;
			}
			
			index++;
		}
		
		return -1;
	}
	
	/**
	 * Removes the element at the specified position (if the specified position is in range of [0, size-1])
	 *  and adjusts backing-array  accordingly so there are no empty positions between elements.
	 * @param index index of the element to be removed
	 * @throws IndexOutOfBoundsException if given position is out of range
	 */
	public void remove(int index) {
		if(index < 0 || index > size-1) {
			throw new IndexOutOfBoundsException();
		}
		
		for(int i = index; i < size-1; i++) {
			elements[i] = elements[i+1];
		}
		
		elements[size-1] = null;
		size--;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object value) {
		return indexOf(value) < 0 ? false : true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object value) {
		if(value == null) {
			throw new IllegalArgumentException();
		}
		
		int index;
		if((index = indexOf(value)) >= 0) {
			remove(index);
			return true;
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(elements, size);	
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forEach(Processor processor) {
		if(processor == null) {
			throw new IllegalArgumentException();
		}
		
		for(Object tmp : elements) {
			//if tmp is null, array doesn't contain
			//any more elements
			if(tmp == null) {
				break;
			}
			
			processor.process(tmp);
		}
	}
}
