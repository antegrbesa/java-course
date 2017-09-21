package hr.fer.zemris.java.hw11.jnotepadapp.local;

/**
 * Interface for localization providers. 
 * @author Ante Grbeša
 *
 */
public interface ILocalizationProvider {

	/**
	 * Uses internal resource bundle to translate requested key. 
	 * @param key key to translate
	 * @return translated string
	 */
	String getString(String key);
	
	/**
	 * Adds a localization listener to this class.
	 * @param e listener to add
	 */
	void addLocalizationListener(ILocalizationListener e);
	
	/**
	 * Removes a localization listener from this class.
	 * @param e listener to remove
	 */
	void removeLocalizationListener(ILocalizationListener e);
	
	
}
