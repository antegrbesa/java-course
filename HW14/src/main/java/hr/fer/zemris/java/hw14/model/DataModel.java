package hr.fer.zemris.java.hw14.model;

/**
 * Models an entry from pollsOptions table. 
 * @author Ante Grbeša
 *
 */
public class DataModel {

	/**ID of entry*/
	private long id;
	
	/**Entry title*/
	private String optionTitle;
	
	/**Entry link */
	private String optionLink;

	/**Entry poll ID*/
	private long pollID;

	/**Entry votes count*/
	private long votesCount;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the optionTitle
	 */
	public String getOptionTitle() {
		return optionTitle;
	}

	/**
	 * @param optionTitle
	 *            the optionTitle to set
	 */
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	/**
	 * @return the optionLink
	 */
	public String getOptionLink() {
		return optionLink;
	}

	/**
	 * @param optionLink
	 *            the optionLink to set
	 */
	public void setOptionLink(String optionLink) {
		this.optionLink = optionLink;
	}

	/**
	 * @return the pollID
	 */
	public long getPollID() {
		return pollID;
	}

	/**
	 * @param pollID
	 *            the pollID to set
	 */
	public void setPollID(long pollID) {
		this.pollID = pollID;
	}

	/**
	 * @return the votesCount
	 */
	public long getVotesCount() {
		return votesCount;
	}

	/**
	 * @param votesCount
	 *            the votesCount to set
	 */
	public void setVotesCount(long votesCount) {
		this.votesCount = votesCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DataModel))
			return false;
		DataModel other = (DataModel) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
