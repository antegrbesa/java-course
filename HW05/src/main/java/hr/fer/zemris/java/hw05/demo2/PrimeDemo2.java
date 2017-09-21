package hr.fer.zemris.java.hw05.demo2;

/**
 * Demonstration class for {@link PrimesCollection}.
 * @author Ante
 *
 */
public class PrimeDemo2 {
	
	/**
	 * This method starts once the program has started.
	 * @param args not used
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(2);
		for(Integer prime : primesCollection) {
			for(Integer prime2 : primesCollection) {
				System.out.println("Got prime pair: "+prime+", "+prime2);
			}
		}
	}
}
