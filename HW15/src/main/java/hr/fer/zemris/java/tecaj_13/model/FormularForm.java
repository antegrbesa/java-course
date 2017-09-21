package hr.fer.zemris.java.tecaj_13.model;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.hw15.util.CryptoUtil;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;


/**
 * Class models a registration form for creating a new {@link BlogUser}. It contains a map of errors where all errors that
 * occured are saved in. 
 * @author Ante
 *
 */
public class FormularForm {

	/**Id of user*/
	private String id;
	
	/**
	 * User lastname.
	 */
	private String lastName;
	
	/**
	 * First name of user
	 */
	private String firstName;
	
	/**
	 * Users' email.
	 */
	private String email;
	
	/**
	 * Nickname
	 */
	private String nick;
	
	/**
	 * Password for this user
	 */
	private String password;
	
	/**
	 * Minimal number of letters for password.
	 */
	private static final int MIN_PASSWORD = 5;

	/**Map of errors.*/
	Map<String, String> errors = new HashMap<>();
	
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
	 * Fills this form from http request. 
	 * @param req request to use
	 */
	public void FillFromHttpRequest(HttpServletRequest req) {
		this.id = prepare(req.getParameter("id"));
		this.firstName = prepare(req.getParameter("firstName"));
		this.lastName = prepare(req.getParameter("lastName"));
		this.email = prepare(req.getParameter("email"));
		this.nick = prepare(req.getParameter("nick"));
		this.password = prepare(req.getParameter("password"));
	}

	/**
	 * Fills this form from given blog user.
	 * @param user user to use
	 */
	public void FillFromBlogUser(BlogUser user) {
		if(user.getId()==null) {
			this.id = "";
		} else {
			this.id = user.getId().toString();
		}
		
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.nick = user.getNick();
		this.password = user.getPasswordHash();
	}

	/**
	 * Fills given user with data from this form.
	 * @param r user to fill
	 */
	public void FillInUser(BlogUser r) {
		if(this.id.isEmpty()) {
			r.setId(null);
		} else {
			r.setId(Long.valueOf(this.id));
		}
		
		r.setFirstName(this.firstName);
		r.setLastName(this.lastName);
		r.setEmail(this.email);
		r.setNick(this.nick);
		try {
			r.setPasswordHash(CryptoUtil.getDigest(this.password));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException ignorable) {
		}
		
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
		
		if (this.firstName.isEmpty()) {
			errors.put("firstName", "First name must not be empty");
		}
		
		if (this.lastName.isEmpty()) {
			errors.put("lastName", "Last name must not be empty");
		}

		if (this.nick.isEmpty()) {
			errors.put("nick", "Nick must not be empty");
		} else if (this.nick.contains("/")) {
			errors.put("nick", "Nick contains invalid character /");
		} else if (DAOProvider.getDAO().containsNick(nick)) {
			errors.put("nick", "Nick "+nick+" already exists. Please choose a different one.");
		}

		if (this.password.length() < MIN_PASSWORD) {
			errors.put("password", "Password must contain at least 5 characters");
		}
		
		if(this.email.isEmpty()) {
			errors.put("email", "EMail is mandatory!");
		} else {
			int l = email.length();
			int p = email.indexOf('@');
			if(l<3 || p==-1 || p==0 || p==l-1) {
				errors.put("email", "EMail not properly formatted");
			}
		}
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
	 * @return the lastName
	 */
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
	 * @return the firstName
	 */
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
	 * @return the email
	 */
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
	 * @return the nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @param nick the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the errors
	 */
	public Map<String, String> getErrors() {
		return errors;
	}

	/**
	 * @param errors the errors to set
	 */
	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
	
	

}
