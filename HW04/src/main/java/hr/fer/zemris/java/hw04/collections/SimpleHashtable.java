package hr.fer.zemris.java.hw04.collections;


import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>This class implements a hash table, which maps keys to values. 
 * Any non-null object can be used as a key or as a value. To successfully store and 
 * retrieve objects from a hashtable, the objects used as keys must implement the hashCode
 * method and the equals method.  Note that the hash table is open: in the case of a "hash collision",
 * a single bucket stores multiple entries.
 * </p>
 *  
 * <p>Default capacity of this hashtable is 16. A single bucket (or entry) is modeled with {@link TableEntry}
 * class. 
 * </p>
 * 
 * <p>General complexity for adding elements is usually O(1) (if resizing is not necessary). General complexity for retrieval of an 
 * element is usually O(1) (if a bucket contains more than one entry, complexity is different). Performance of this hashtable is affected by
 * two parameters, <code>initial capacity</code> and <code>load factor</code>. <code>Initial capacity</code> is the maximum capacity at the time table
 * is created.  The load factor is a measure of how full the hash table is allowed to get before its capacity is automatically increased. 
 * The initial capacity and load factor parameters are merely hints to the implementation. The exact details as to when and whether 
 * the rehash method is invoked are implementation-dependent.
 * </p>
 * 
 * 
 * 
 * <p>The iterators returned by the iterator method of this class are fail-fast:  if the Hashtable is structurally modified at any time 
 * after the iterator is created, in any way except through the iterator's own remove method,
 * the iterator will throw a {@link ConcurrentModificationException}.
 *  </p>
 * @author Ante Grbesa
 *
 * @param <K> key paramter
 * @param <V> value paramter
 */
public class SimpleHashtable<K, V> 
	implements Iterable<SimpleHashtable.TableEntry<K, V>>{
	
	/**Default size for default constructor*/
	private static final int DEFAULT_SIZE = 16;
	
	/**Default load factor*/
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;
	
	/**Internal collection for storing elements*/
	private TableEntry<K, V>[] table;
	
	/**Number of elements in collection*/
	private int size;
	
	/**Number of modifications of this collection*/
	private int modificationCount;

	/**
	 * Default constructor for SimpleHashtable. Capacity of this collection is
	 * set to 16.
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable() {
		this.size = 0;
		this.table = (TableEntry<K, V>[]) new TableEntry<?, ?>[DEFAULT_SIZE];
	}

	/**
	 * Constructs a SimpleHashtable with specified capacity.
	 * @param userSize specifies initial capacity of this collection
	 * @throws IllegalArgumentException if specified size is less than 1
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int userSize) {
		if(userSize < 1) {
			throw new IllegalArgumentException("Argument size must be greater than zero.");
		}
		this.size = 0;
		this.table = (TableEntry<K, V>[]) new TableEntry<?, ?>[getFirstSuperiorPower(userSize)];
	}

	/**
	 * Method for getting first greater power of 2 of a number.
	 * 
	 * @param number of comparing.
	 * @return number first greater power of 2.
	 */
	public static int getFirstSuperiorPower(int number) {
		int x = (number == 0 ? 0 : 32 - Integer.numberOfLeadingZeros(number - 1));
		return (int) Math.pow(2, x);
	}
	
	/**
	 * Calculates hash code of a slot for given key.
	 * @param key key for calculating code.
	 * @return hash code representation of a key.
	 */
	private int CalculateSlotCode(Object key) {
		return Math.abs(key.hashCode()) % table.length;
	}
	
	/**
	 * Checks if size is at it's limit (75% of maximum capacity). If it is,
	 * this method resizes backing array and adds all elements to it. 
	 */
	@SuppressWarnings("unchecked")
	private void checkSize() {
		if(size >= table.length * DEFAULT_LOAD_FACTOR) {
			TableEntry<K,V>[] copy = table;
			int maxSize  = table.length * 2;
			table = (TableEntry<K, V>[]) new TableEntry<?, ?>[maxSize];
			size = 0;
			
			for(TableEntry<K, V> entry : copy) {
				while(entry != null) {
					put(entry.getKey(), entry.getValue());
					entry = entry.next;
				}
			}
		}
	}
	
	/**
	 * Puts a (key, value) pair to SimpleHashtable. Key must be non-null element.
	 * @param key key to put
	 * @param value value to put
	 * @throws IllegalArgumentException if given key is null
	 */
	public void put(K key, V value) {
		if(key == null) {
			throw new IllegalArgumentException("Key must not be null.");
		}

		int slotCode = CalculateSlotCode(key);

		if(table[slotCode] == null) {
			table[slotCode] = new TableEntry<K, V>(key, value, null);
			size++;
			modificationCount++;
			checkSize();
		} else {
			TableEntry<K, V> entry = table[slotCode];
			while(entry.next != null && entry.getKey() != key) {
				entry = entry.next;
			}

			if(entry.getKey().equals(key)) {
				entry.setValue(value);
			} else {
				entry.next = new TableEntry<K, V>(key, value, null);
				size++;
				modificationCount++;
				checkSize();
			}	
		}
	}

	/**
	 * Searches this table for a pair with given key.
	 * @param key key to search for
	 * @return value for specified key, or null if such was not found
	 *
	 */
	public V get(Object key) {
		if(key == null) {
			return null;
		}

		int slotCode = CalculateSlotCode(key);
		if (table[slotCode] == null) {
			return null;
		}

		TableEntry<K, V> entry = table[slotCode];
		while(entry != null && !entry.getKey().equals(key)) {
			entry = entry.next;
		}

		if(entry == null) {
			return null;
		} else {
			return entry.getValue();
		}
	}

	/**
	 * Method for getting size of table.
	 * @return size of hash table.
	 */
	public int size() {
		return size;
	}

	/**
	 * Checks if table contains specified key.
	 * @param key key to check
	 * @return true if contains, false otherwise.
	 */
	public boolean containsKey(Object key) {
		if(key == null) {
			return false;
		}
		
		for(TableEntry<K, V> entry : table) {
			while(entry != null) {
				if(entry.getKey().equals(key)) {
					return true;
				}
				entry = entry.next;
			}
		}
		
		return false;
	}

	/**
	 * Checks if table contains specified value.
	 * @param value value to check
	 * @return true if contains, false otherwise.
	 */
	public boolean containsValue(Object value) {
		for(TableEntry<K, V> entry : table) {
			while(entry != null) {
				if(entry.getValue().equals(value)) {
					return true;
				}
				entry = entry.next;
			}
		}
		
		return false;
	}

	/**
	 * Removes element with given key from table, if element
	 * with specified key doesn't exist, method does nothing.
	 * @param key Key of the element to remove
	 */
	public void remove(Object key) {
		if(key == null) {
			return;
		}
		
		int slotCode = 	CalculateSlotCode(key);
		try {
			if(table[slotCode] != null) {

				TableEntry<K, V> current = table[slotCode];
				TableEntry<K, V> previous = null;
				while(current.next != null && current.getKey() != key) {
					previous = current;
					current = current.next;
				}
				if (current.getKey() == key) {
					if(previous == null) {
						table[slotCode] = current.next;
					}
					else {
						previous.next = current.next;
					}
					size--;
					modificationCount++;
				}
			}	
		} catch(NoSuchElementException ex) {
			System.out.println("There is no such key.");
		}
	}

	/**
	 * Clears all elements from this table.
	 */
	public void clear() {
		if(size > 0) {
			size = 0;
			for(int i = 0; i < table.length; i++) {
				table[i] = null;
			}
		}
		modificationCount++;
	}
	
	/**
	 * Checks if table is empty.
	 * @return true if its empty, false otherwise
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	/**
	 * Creates string representation of elements in this hash table.
	 * @return string representation of elements
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(TableEntry<K, V> entry : table) {
			while(entry != null) {
				sb.append(entry.toString() + "%n");
				entry = entry.next;
			}
		}
		return sb.toString();
	}
	
	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}
	
	/**
	 * Implementation of {@link Iterator} class for SimpleHashtable.
	 * @author Ante Grbesa
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K,V>> {
		/**Index of current element*/
		
		private int currentIndex;
		/**Modification count*/
		private int modCount;
		
		/**Next element for retrieval*/
		private TableEntry<K, V> currentElement;
		
		/**Last returned element*/
		private TableEntry<K, V> returnElement;
		
		/**
		 * Constructs iterator.
		 */
		public IteratorImpl() {
			currentIndex = 0;
			modCount = modificationCount;
			currentElement = returnElement = table[0];
		}
		
		@Override
		public boolean hasNext() {
			while(currentElement == null) {
				currentIndex++;
				if(currentIndex < table.length) {
					currentElement = table[currentIndex];
				} else {
					return false;
				}
			}
			
			return true;
					
		}

		@Override
		public TableEntry<K, V> next() {
			if(! hasNext()) {
				throw new NoSuchElementException("No more elements.");
			}
			if(modCount != modificationCount) {
				throw new ConcurrentModificationException();
			}
			
			returnElement = currentElement;
			
			if(returnElement.next != null) {
				currentElement = returnElement.next;
			} else {
				try {
					currentElement = null;
				} catch(IndexOutOfBoundsException e) {
					throw new NoSuchElementException("No more elements.");
				}
			}
			
			return returnElement;	
		}
		
		@Override
		public void remove() {
			if(returnElement == null) {
				throw new IllegalStateException();
			}
			if(modCount != modificationCount) {
				throw new ConcurrentModificationException();
			}
			
			SimpleHashtable.this.remove(returnElement.getKey());
			modCount = modificationCount;
			returnElement = null;
			
		}
		
	}
	

	/**
	 * TableEntry is a simple data structure for hash table. Represents a 
	 * single entry. Holds parameterized key and value as well as reference
	 *  to next slot,  which is used for solving hash table collisions.
	 * @param <K> specifies parameterized key
	 * @param <V> specifies parameterized value
	 */
	public static class TableEntry<K, V> {
		/**Key*/
		private final K key;
		
		/**Value*/
		private V value;
		
		/**Next element*/
		private TableEntry<K, V> next;

		/**
		 * Creates single table entry.
		 * @param key key to assign to entry.
		 * @param value value to assign to entry
		 * @param next pointer to the next entry element.
		 */
		public TableEntry(K key, V value, TableEntry<K, V> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}

		/**
		 * Returns value of entry.
		 * @return the value of entry
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Sets value of entry.
		 * @param value the value to set
		 */
		public void setValue(V value) {
			this.value = value;
		}

		/**
		 * Returns key of entry.
		 * @return the key of entry
		 */
		public K getKey() {
			return key;
		}

		/**
		 * Creates string representation of this entry.
		 * @return String representation of entry.
		 */
		@Override
		public String toString() {
			return String.format("%s=%s ", key.toString(), value.toString());


		}
	}

	

}
