package hr.fer.zemris.java.tecaj_13.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Models a single blog entry.
 * @author Ante
 *
 */
@Entity
@Table(name="blog_entries")
@NamedQueries({
	@NamedQuery(name="BlogEntry.upit1",query="select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when")
})
@Cacheable(true)
public class BlogEntry {

	/**Id of entry*/
	private Long id;
	
	/**List of comments*/
	private List<BlogComment> comments = new ArrayList<BlogComment>();
	
	/**Date of creation*/
	private Date createdAt;
	
	/**Date of last modification*/
	private Date lastModifiedAt;
	
	/**Title of blog*/
	private String title;
	
	/**Text of blog*/
	private String text;
	
	/**Author of blog*/
	private BlogUser creator;
	
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
	 * Gets comments for this blog
	 * @return comments
	 */
	@OneToMany(mappedBy="blogEntry", fetch=FetchType.LAZY, cascade=CascadeType.PERSIST, orphanRemoval=true)	
	@OrderBy("postedOn")
	public List<BlogComment> getComments() {
		return comments;
	}
	
	/**
	 * Sets comments for this blog
	 * @param comments to set
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}
	
	/**
	 * Adds a single comment to this blog. 
	 * @param com comment to add
	 */
	public void addComment(BlogComment com) {
		comments.add(com);
	}


	/**
	 * Gets date of entry creation.
	 * @return date of entry
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets creation date.
	 * @param createdAt date  to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Returns date of last modification.
	 * @return date of last modification
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Sets date of last modification.
	 * @param lastModifiedAt date to set
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * 
	 * @return title of bblog
	 */
	@Column(length=200,nullable=false)
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return text of blog
	 */
	@Column(length=4096,nullable=false)
	public String getText() {
		return text;
	}

	/**
	 * 
	 * @param text text to set
	 */
	public void setText(String text) {
		this.text = text;
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	/**
	 * 
	 * @return author of blog
	 */
	@ManyToOne
	@JoinColumn(nullable=false)
	public BlogUser getCreator() {
		return creator;
	}

	/**
	 * 
	 * @param creator author to set
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
	}

	/**
	 * Updates this entry using another entry.
	 * @param e entry to use
	 */
	public void update(BlogEntry e) {
		this.title = e.title;
		this.text = e.text;
		this.lastModifiedAt = new Date();	
	}
}