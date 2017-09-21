package hr.fer.zemris.java.tecaj_13.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class models a single comment on a blog. 
 * @author Ante Grbeša
 *
 */
@Entity
@Table(name="blog_comments")
public class BlogComment {
	
	/**Id of comment*/
	private Long id;
	
	/**Parent entry of this comment*/
	private BlogEntry blogEntry;
	
	/**User's email*/
	private String usersEMail;
	
	/**Message of comment*/
	private String message;
	
	/**Post time of comment*/
	private Date postedOn;
	
	/**
	 * @return id
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * @param id id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets parent blog entry. 
	 * @return blog entry
	 */
	@ManyToOne
	@JoinColumn(nullable=false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}
	
	/**
	 * Sets a blog entry
	 * @param blogEntry to set
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Gets users email.
	 * @return users' email
	 */
	@Column(length=100,nullable=false)
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Sets email of user.
	 * @param usersEMail email to set
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * Gets message of comment.
 	 * @return message of comment
	 */
	@Column(length=4096,nullable=false)
	public String getMessage() {
		return message;
	}

	/**
	 * Sets message of comment
	 * @param message message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets date of comment creation.
	 * @return date of comment
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Sets posted date
	 * @param postedOn date to set
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}