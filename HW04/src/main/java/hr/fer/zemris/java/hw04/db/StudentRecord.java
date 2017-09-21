package hr.fer.zemris.java.hw04.db;

/**
 * This class represents a single student. Each student is differentiated by it's 
 * <i>JMBAG</i> value.  
 * @author Ante
 *
 */
public class StudentRecord {
	/**JMBAG value of a student*/
	private String jmbag;
	
	/**First name of a student*/
	private String firstName;
	
	/** Last name of a student*/
	private String lastName;
	
	/**Final grade of a student*/
	private final int finalGrade;

	/**
	 * Creates an instance of this class with specified values for a student. 
	 * @param jmbag jmbag of a student
	 * @param firstName first name of a student
	 * @param lastName last name of a student
	 * @param finalGrade final grade of a student
	 * @throws IllegalArgumentException if any of  given arguments is null, excluding finalGrade
	 */
	public StudentRecord(String jmbag, String firstName, String lastName, int finalGrade) {
		if(jmbag == null || firstName == null || lastName == null) {
			throw new IllegalArgumentException("Null value not allowed");
		}
		this.jmbag = jmbag;
		this.firstName = firstName;
		this.lastName = lastName;
		this.finalGrade = finalGrade;
	}

	/**
	 * Gets the jmbag.
	 * @return the jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Sets the jmbag.
	 * @param jmbag the jmbag to set
	 */
	public void setJmbag(String jmbag) {
		this.jmbag = jmbag;
	}

	/**
	 * Gets the first name.
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name.
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the final grade.
	 * @return the finalGrade
	 */
	public int getFinalGrade() {
		return finalGrade;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentRecord other = (StudentRecord) obj;
		if (jmbag == null) {
			if (other.jmbag != null)
				return false;
		} else if (!jmbag.equals(other.jmbag))
			return false;
		return true;
	}
	
}
