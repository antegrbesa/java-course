package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A special kind of <i>Map</i>  that  allows the user to store multiple values 
 * for same key and provides a stack-like abstraction for storing multiple values to the same
 * key. Keys are instances of String and values are instances of {@link ValueWrapper} class.
 * Null values and null keys aren't allowed. 
 * @author Ante
 *
 */
public class ObjectMultistack {
	
	/**Internal collection of elements*/
	private Map<String, MultistackEntry> elements;
	
	/**
	 * Creates an instance of this class. 
	 */
	public ObjectMultistack() {
		elements = new HashMap<>();
	}
	
	/**
	 * Method for checking if given arguments are null
	 * @param arguments arguments to check
	 */
	private  void checkArgumentsForNull(Object ...arguments) {
		for(Object o : arguments) {
			if(o == null) {
				throw new IllegalArgumentException("Value was null");
			}
		}
	}
	
	/**
	 * Checks if stack under specified key contains an element.
	 * @param name key to check
	 * @return null if given key doesn't exist, else returns entry
	 * under given key
	 * @throws NoSuchElementException if stack under given key is empty(null)
	 */
	private MultistackEntry checkStackForElement(String name) {
		if(! elements.containsKey(name)) {
			return null;
		}
		
		
		MultistackEntry entry = elements.get(name);
		if(entry == null) {
			throw new NoSuchElementException("Stack was empty");
		}
		
		return entry;
	}
	
	/**
	 * Pushes specified ValueWrapper to stack under given key. 
	 * @param name key of stack to push element to 
	 * @param valueWrapper element to push to stack
	 * @throws IllegalArgumentException if any of given arguments
	 * are null
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		checkArgumentsForNull(name, valueWrapper);
		
		MultistackEntry entry = new MultistackEntry(valueWrapper);
		if(elements.containsKey(name)) {
			entry.next = elements.get(name);
			elements.put(name, entry);
			return;
		} else {
			elements.put(name, new MultistackEntry(valueWrapper));
		}
	}
	
	/**
	 * Pops the top element from stack under specified key. 
	 * @param name key of a stack 
	 * @return ValueWrapper element from top of the stack, or
	 * null if given key doesn't exist
	 * @throws IllegalArgumentException if given key is null
	 * @throws NoSuchElementException if specified stack is empty
	 */
	public ValueWrapper pop(String name) {
		checkArgumentsForNull(name);
		
		MultistackEntry entry = checkStackForElement(name);
		if(entry == null) {
			return null;
		}
		
		MultistackEntry tmp = entry.next;
		elements.put(name, tmp);
		
		return entry.value;
	}
	
	/**
	 * Returns element currently on top of stack under specified key. 
	 * @param name key of a stack
	 * @return top element on stack, or null if given key is not in table
	 * @throws IllegalArgumentException if given key is null
	 * @throws NoSuchElementException if specified stack is empty
	 */
	public ValueWrapper peek(String name) {
		checkArgumentsForNull(name);
		
		MultistackEntry entry = checkStackForElement(name);
		if(entry == null) {
			return null;
		}
		
		return entry.value;	
	}
	
	/**
	 * Checks if specified stack is empty. 
	 * @param name key of a stack
	 * @return true if specified stack is empty or if given key is 
	 * not in map, false otherwise
	 */
	public boolean isEmpty(String name) {
		checkArgumentsForNull(name);
		
		return elements.get(name) == null ? true : false;
	}
	
	
	
	/**
	 * This class acts as a node of a single-linked list.
	 * @author Ante
	 *
	 */
	private static class MultistackEntry {
		/**Value of node*/
		private ValueWrapper value;
		
		/**Reference to the next node*/
		private MultistackEntry next;
		
		/**
		 *Creates a node with specified value. 
		 *@param value value to set
		 */
		public MultistackEntry(ValueWrapper value) {
			this.value = value;
		}
	}
}
