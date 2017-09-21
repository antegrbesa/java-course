package hr.fer.zemris.java.tecaj_13.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;

/**
 * Models a form for blog creation. 
 * @author Ante Grbeša
 *
 */
public class BlogForm {

	/**Creation date*/
	private Date createdAt;
	
	/**Date of last modification*/
	private Date lastModifiedAt;
	
	/**Title of blog*/
	private String title;
	
	/**Text of blog*/
	private String text;
	
	/**Author of blog*/
	private BlogUser creator;
	
	/**Id of entry*/
	private String id;
	
	/**
	 * Map of errors. Keys are names of fields and values are error texts.
	 */
	private Map<String, String> errors = new HashMap<>();
	/**
	 * @return the createdAt date
	 */
	public Date getCreatedAt() {
		return createdAt;
	}
	/**
	 * @param createdAt the createdAt date to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	/**
	 * @return the lastModifiedAt date
	 */
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}
	/**
	 * @param lastModifiedAt the lastModifiedAt date to set
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @return the author
	 */
	public BlogUser getCreator() {
		return creator;
	}
	/**
	 * @param creator the author to set
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
	}
	
	/**
	 * Gets map of errors.
	 * @return map of errors.
	 */
	public Map<String, String> getErrors() {
		return errors;
	}
	
	/**
	 * Gets error message for given property
	 * 
	 * @param property name of property
	 * @return error message or <code>null</code> if property doesn't have an error
	 */
	public String getError(String property) {
		return errors.get(property);
	}
	
	/**
	 * Checks whether there is an error for any property.
	 * 
	 * @return <code>true</code> if it has, <code>false</code> otherwise.
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	/**
	 *  Checks whether there is an error for given property. 
	 * 
	 * @param property name of property
	 * @return <code>true</code> if it has, <code>false</code> otherwise.
	 */
	public boolean hasError(String property) {
		return errors.containsKey(property);
	}
	
	/**
	 * Fills this form from given blog entry.
	 * @param entry entry to use
	 */
	public void fillFromEntry(BlogEntry entry) {
		if(entry.getId()==null) {
			this.id = "";
		} else {
			this.id = entry.getId().toString();
		}
		
		this.title = entry.getTitle();
		this.creator = entry.getCreator();
		this.text = entry.getText();
		this.lastModifiedAt = new Date();
		this.createdAt = entry.getCreatedAt();
	}
	
	/**
	 * Fills this form from http request. 
	 * @param req request to use
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		this.id = prepare(req.getParameter("id"));
		this.title = prepare(req.getParameter("title"));
		this.text = prepare(req.getParameter("text"));
		this.lastModifiedAt = new Date();
		
		this.creator = DAOProvider.getDAO().getBlogUserByNick
					((String) req.getSession().getAttribute("user_nick"));
		
		this.createdAt = this.lastModifiedAt;
		
	}
	
	/**
	 * Fills given entry with data from this form.
	 * @param r entry to fill
	 */
	public void fillEntry(BlogEntry r) {
		if(this.id.isEmpty()) {
			r.setId(null);
		} else {
			r.setId(Long.valueOf(this.id));
		}
		
		r.setTitle(this.title);
		r.setText(this.text);
		r.setCreator(this.creator);
		r.setCreatedAt(this.createdAt);
		r.setLastModifiedAt(this.lastModifiedAt);
	}
	
	/**
	 * Helper method that converts null strings into empty strings
	 * 
	 * @param s string
	 * @return given string if it's different from <code>null</code>, empty string otherwise.
	 */
	private String prepare(String s) {
		if(s==null) return "";
		return s.trim();
	}
	
	/**
	 * Validates all properties from this class. If a property has an error, it is 
	 * saved in errors map. 
	 */
	public void validate() {
		errors.clear();
		
		if (!this.id.isEmpty()) {
			try {
				Long.parseLong(this.id);
			} catch(NumberFormatException ex) {
				errors.put("id", "ID value not valid");
			}
		}
		
		if (title.isEmpty()) {
			errors.put("title", "Title must not be empty!");
		}

	}
		
}
