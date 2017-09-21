package hr.fer.zemris.java.tecaj_13.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;

/**
 * Models a form for creating a new comment. 
 * @author Ante Grbeša
 *
 */
public class CommentForm {

	/**Id of comment*/
	private String id;
	
	/**Parent entry of this comment*/
	private BlogEntry blogEntry;
	
	/**User's email*/
	private String usersEMail;
	
	/**Message of comment*/
	private String message;
	
	/**Post time of comment*/
	private Date postedOn;
	
	/**Map of errors.*/
	private Map<String, String> errors = new HashMap<>();
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the blogEntry
	 */
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}
	/**
	 * @param blogEntry the blogEntry to set
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}
	/**
	 * @return the usersEMail
	 */
	public String getUsersEMail() {
		return usersEMail;
	}
	/**
	 * @param usersEMail the usersEMail to set
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the postedOn date
	 */
	public Date getPostedOn() {
		return postedOn;
	}
	/**
	 * @param postedOn the postedOn date to set
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}
	
	/**
	 * Gets map of errors.
	 * @return errors map
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
	 * Fills this form from given blog comment.
	 * @param entry entry to use
	 */
	public void fillFromComment(BlogComment entry) {
		if(entry.getId()==null) {
			this.id = "";
		} else {
			this.id = entry.getId().toString();
		}
		
		this.message = entry.getMessage();
		this.postedOn = entry.getPostedOn();
		this.usersEMail = entry.getUsersEMail();
		this.blogEntry = entry.getBlogEntry();
	}
	
	/**
	 * Fills this form from http request. 
	 * @param req request to use
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		this.id = prepare(req.getParameter("id"));
		this.message = prepare(req.getParameter("message"));
		this.usersEMail = prepare(req.getParameter("usersEMail"));
		this.postedOn = new Date();
		String eid = (String) req.getSession().getAttribute("eid");
		this.blogEntry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(eid));
		
	}
	
	/**
	 * Fills given comment with data from this form.
	 * @param r comment to fill
	 */
	public void fillComment(BlogComment r) {
		if(this.id.isEmpty()) {
			r.setId(null);
		} else {
			r.setId(Long.valueOf(this.id));
		}
		
		r.setBlogEntry(this.blogEntry);
		r.setMessage(this.message);
		r.setPostedOn(this.postedOn);
		r.setUsersEMail(usersEMail);
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
		
		if (message.isEmpty()) {
			errors.put("message", "Message must not be empty!");
		}

		if (usersEMail.isEmpty()) {
			errors.put("usersEMail", "E-mail must not be empty!");
		} else {
			int l = usersEMail.length();
			int p = usersEMail.indexOf('@');
			if(l<3 || p==-1 || p==0 || p==l-1) {
				errors.put("usersEMail", "EMail not properly formatted");
			}
		}
	}
}
