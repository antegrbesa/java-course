package hr.fer.zemris.java.tecaj_13.model;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.hw15.util.CryptoUtil;
import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;

/**
 * Models a login form. 
 * @author Ante Grbeša
 *
 */
public class LoginForm {

	/**Nickname of user*/
	private String nick;
	
	/**Password of user*/
	private String password;
	
	/**Map of errors*/
	private Map<String, String> errors = new HashMap<>();

	/**
	 * @return the nickname
	 */
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
	 * Fills this form from http request. 
	 * @param req request to use
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		nick = prepare(req.getParameter("nick"));
		password = prepare(req.getParameter("password"));
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
		if (! DAOProvider.getDAO().containsNick(nick)) {
			errors.put("nick", "Provided nickname doesn't exist!");
		}
		if (password.isEmpty()) {
			errors.put("password", "Password must not be empty");
		} 
		
		String digest;
		try {
			digest = DAOProvider.getDAO().getBlogUserByNick(nick).getPasswordHash();
		} catch (IllegalArgumentException e) {
			return;
		}
		
		try {
			if (! digest.equals(CryptoUtil.getDigest(password))) {
				errors.put("password", "Password does not match given username");
			}
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException ignorable) {
		}
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
	
}
