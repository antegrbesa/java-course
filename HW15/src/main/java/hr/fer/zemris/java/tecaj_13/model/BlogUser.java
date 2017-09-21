package hr.fer.zemris.java.tecaj_13.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Models a single blog user. 
 * @author Ante Grbeša
 *
 */
@Entity
@Table(name="blog_users")
@Cacheable(true)
public class BlogUser {

	/**Id of entry*/
	private Long id;
	
	/**First name of user*/
	private String firstName;
	
	/**Last name of user*/
	private String lastName;
	
	/**Nickname of user*/
	private String nick;
	
	/**Email of user*/
	private String email;
	
	/**Password hash*/
	private String passwordHash;
	
	/**Blogs from this user*/
	private List<BlogEntry> blogs = new ArrayList<>();

	/**
	 * @return the id
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the firstName 
	 */
	@Column(nullable=false, length=30)
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	@Column(nullable=false, length=50)
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the nickname
	 */
	@Column(nullable=true, length=20, unique=true)
	public String getNick() {
		return nick;
	}

	/**
	 * @param nick the nickname to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return the email
	 */
	@Column(length=100,nullable=false)
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the passwordHash
	 */
	@Column(length=40,nullable=false)
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * @param passwordHash the passwordHash to set
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	/**
	 * Gets all blogs from this user.
	 * @return all blogs
	 */
	@OneToMany(mappedBy="creator", fetch=FetchType.LAZY)
	public List<BlogEntry> getBlogs() {
		return blogs;
	}
	
	/**
	 * Adds a blog to this user.
	 * @param blog blog to add
	 */
	public void addBlog(BlogEntry blog) {
		blogs.add(blog);
	}
	
	/**
	 * Sets blogs for this user.
	 * @param blogs blogs to set.
	 */
	public void setBlogs(List<BlogEntry> blogs) {
		this.blogs = blogs;
	}
}
