package hr.fer.zemris.java.hw05.demo2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class that calculates n prime numbers using iterator. N is given in
 * constructor.
 * @author Ante
 *
 */
public class PrimesCollection implements Iterable<Integer> {
	
	/**Number of primes to produce*/
	private int numberOfPrimes;
	
	/**
	 * Constructor for this class. 
	 * @param numberOfPrimes number of primes to produce
	 */
	public PrimesCollection(int numberOfPrimes) {
		this.numberOfPrimes = numberOfPrimes;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new IteratorImpl(numberOfPrimes);
	}
	
	/**
	 * Implementation of a {@link Iterator} that produces prime number in
	 * every iteration. 
	 * @author Ante
	 *
	 */
	private class IteratorImpl implements Iterator<Integer> {
		
		/**Number of iterations*/
		private int n;
		
		/**Last prime produced*/
		private int lastPrime;
		
		/**
		 * Constructs an iterator wtih specfied maximal number of iterations
		 * @param n number of iterations
		 */
		public IteratorImpl(int n) {
			super();
			if(n<0) {
				throw new IllegalArgumentException("Negative value");
			}
			this.n = n;
			lastPrime = 1;
		}

		@Override
		public boolean hasNext() {
			return n > 0;
		}

		@Override
		public Integer next() {
			if(! hasNext()) {
				throw new NoSuchElementException();
			}
			
			n--;
			return calculatePrime();
		}
		
		/**
		 * Method for calculation of a prime number. 
		 * @return
		 */
		private int calculatePrime() {
			if(lastPrime == 1) {
				lastPrime += 2;
				return 2;
			}
			
			while(true) {
				boolean prime = true;
				for(int i = 3; i*i<=lastPrime; i+=2) {
					if(lastPrime%i==0) {
						prime = false;
						break;
					}
				}
				if(prime) {
					int retValue = lastPrime;
					lastPrime += 2;
					return retValue;
				}
				
				lastPrime += 2;
			}
		}
		
	}
	
	

}
