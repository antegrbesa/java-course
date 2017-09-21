package hr.fer.zemris.java.hw05.demo2;

/**
 * Demonstration class for {@link PrimesCollection}.
 * @author Ante
 *
 */
public class PrimesDemo1 {

	/**
	 * This method starts once the program has started.
	 * @param args not used
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(20); // 5: how many of them
		for(Integer prime : primesCollection) {
		    System.out.println("Got prime: "+prime);
		}

	}

}
