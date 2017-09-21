package hr.fer.zemris.java.hw11.jnotepadapp.local;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Represents a singleton class (Singleton design pattern) used for getting translation. Derived
 * from {@link AbstractLocalizationProvider}.
 * @author Ante
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider{

	/**Instance of this class*/
	private static LocalizationProvider instance;
	
	/**Currently used language*/
	private String language;
	
	/**Bundle containing translations*/
	private ResourceBundle bundle;
	
	/**
	 * Private constructor. 
	 */
	private  LocalizationProvider() {
		language = "en";
		bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadapp.local.translations", Locale.forLanguageTag(language));
	}
	
	/**
	 * Returns an instance of {@link LocalizationProvider}.
	 * @return instance of this class
	 */
	public static LocalizationProvider getInstance() {
		if (instance == null) {
			instance = new LocalizationProvider();
		}
		
		return instance;
	}
	
	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	/**
	 * Sets to language to specified one.
	 * @param language language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
		bundle =  ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadapp.local.translations", Locale.forLanguageTag(language));
		fire();
	}
	
}
