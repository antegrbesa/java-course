/**
 * 
 */
package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.scripting.nodes.ArrayIndexedCollection;


/**
 * Implementation of a last-in-first-out (LIFO) stack of objects. Provides 
 * the usual push and pop stack operations as well as a method to peek at the top
 * item of the stack and a method to test whether stack is empty. 
 * When a stack is first created, it is empty. 
 * @author Ante Grbesa
 *
 */
public class ObjectStack {
	
	/** Underlying collection used for storing elements.*/
	private ArrayIndexedCollection collection;
	
	/**
	 * Creates an empty stack. 
	 */
	public ObjectStack() {
		collection = new ArrayIndexedCollection();
	}

	
	/**
	 * Checks if this stack is empty.
	 * @return true if this stack is empty, false otherwise
	 */
	public boolean isEmpty() {
		return collection.isEmpty();
	}
	
	/**
	 * Returns number of elements in this stack.
	 * @return number of elements
	 */
	public int size() {
		return collection.size();
	}
	
	/**
	 * Pushes given value on the stack. Null values aren't allowed.
	 * @param value value to push
	 * @throws IllegalArgumentException if given value is null
	 */
	public void push(Object value) {
		collection.add(value);
	}
	
	/**
	 * Removes last value pushed on stack from stack and returns it. 
	 * 
	 * @return last value pushed on stack
	 * @throws EmptyStackException if stack is empty
	 */
	public Object pop() {
		if(isEmpty()) {
			throw new EmptyStackException();
		}
		
		int index = collection.size() - 1;
		Object tmp = collection.get(index);
		collection.remove(index);
		
		return tmp;
	}
	
	/**
	 * Returns last element pushed on stack but doesn't delete it 
	 * from the stack. 
	 * @return last element pushed on stack
	 * @throws EmptyStackException if stack is empty
	 */
	public Object peek() {
		if(isEmpty()) {
			throw new EmptyStackException();
		}
		
		return collection.get(collection.size() - 1);
	}
	
	/**
	 * Removes all elements from the stack. 
	 */
	public void clear() {
		collection.clear();
	}
	
}
