package hr.fer.zemris.java.hw05.demo4;

/**
 * Represents a single student. 
 * @author Ante
 *
 */
/**
 * @author Ante
 *
 */
public class StudentRecord {
	/**Jmbag*/
	private String jmbag;
	/**Last name*/
	private String prezime;
	/**Name of a student*/
	private String ime;
	/**Number of points on midterm exam*/
	private double brojBodMI;
	/**Number of points on final midterm*/
	private double brojBodZI;
	/**Number of points on labs*/
	private double brojBodLab;
	/**Final grade*/
	private int ocjena;

	/**
	 * Creates an instace of this class. 
	 * @param jmbag jmbag to set
	 * @param prezime last name to set
	 * @param ime first name to set
	 * @param brojBodMI Number of points on midterm exam to set
	 * @param brojBodZI Number of points on final midterm exam to set
	 * @param brojBodLab Number of points on lab exercises to set
	 * @param ocjena final grade to set
	 */
	public StudentRecord(String jmbag, String prezime, String ime, double brojBodMI, double brojBodZI,
			double brojBodLab, int ocjena) {
		super();
		this.jmbag = jmbag;
		this.prezime = prezime;
		this.ime = ime;
		this.brojBodMI = brojBodMI;
		this.brojBodZI = brojBodZI;
		this.brojBodLab = brojBodLab;
		this.ocjena = ocjena;
	}
	

	
	
	/**
	 * Returns jmbag
	 * @return the jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}




	/**
	 * Sets jmbag
	 * @param jmbag the jmbag to set
	 */
	public void setJmbag(String jmbag) {
		this.jmbag = jmbag;
	}




	/**
	 * Returns last name
	 * @return the prezime
	 */
	public String getPrezime() {
		return prezime;
	}




	/**
	 * Sets last name
	 * @param prezime the prezime to set
	 */
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}




	/**
	 * Returns first name
	 * @return the ime
	 */
	public String getIme() {
		return ime;
	}




	/**
	 * Sets first name
	 * @param ime the ime to set
	 */
	public void setIme(String ime) {
		this.ime = ime;
	}




	/**
	 * Returns points on midterm
	 * @return the brojBodMI
	 */
	public double getBrojBodMI() {
		return brojBodMI;
	}




	/**
	 * Sets points of midterm exam
	 * @param brojBodMI the brojBodMI to set
	 */
	public void setBrojBodMI(double brojBodMI) {
		this.brojBodMI = brojBodMI;
	}




	/**
	 * Returns points on final midterm
	 * @return the brojBodZI
	 */
	public double getBrojBodZI() {
		return brojBodZI;
	}




	/**
	 * Sets points of final midterm
	 * @param brojBodZI the brojBodZI to set
	 */
	public void setBrojBodZI(double brojBodZI) {
		this.brojBodZI = brojBodZI;
	}




	/**
	 * Returns points on lab exercises
	 * @return the brojBodLab
	 */
	public double getBrojBodLab() {
		return brojBodLab;
	}




	/**
	 * Sets points of lab. exercises
	 * @param brojBodLab the brojBodLab to set
	 */
	public void setBrojBodLab(double brojBodLab) {
		this.brojBodLab = brojBodLab;
	}




	/**
	 * Returns grade
	 * @return the ocjena
	 */
	public int getOcjena() {
		return ocjena;
	}




	/**
	 * Sets grade
	 * @param ocjena the ocjena to set
	 */
	public void setOcjena(int ocjena) {
		this.ocjena = ocjena;
	}




	@Override
	public String toString() {
		return String.format("%s %s %s %f %f %f %d", jmbag, prezime, ime
				, brojBodMI, brojBodZI, brojBodLab, ocjena); 
	}
}
