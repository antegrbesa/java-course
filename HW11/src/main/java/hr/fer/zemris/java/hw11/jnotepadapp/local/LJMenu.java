package hr.fer.zemris.java.hw11.jnotepadapp.local;

import javax.swing.JMenu;

/**
 * Custom implementation of a JMenu that changes it's text every time the language is changed. 
 * @author Ante Grbeša
 *
 */
public class LJMenu extends JMenu {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Localization provider
	 */
	private ILocalizationProvider provider;
	
	/**Key for menu name*/
	private String key;

	/**
	 * Constructor for this class. Adds itself as a listener to given localization provider.
	 * @param key key for this class' name
	 * @param provider provider for translation
	 */
	public LJMenu(String key, ILocalizationProvider provider) {
		super();
		this.provider = provider;
		this.key = key;
		updateName();
		provider.addLocalizationListener(() -> { 
			updateName();
		});
	}
	
	/**
	 * Update menu text.
	 */
	private void updateName() {
		setText(provider.getString(key));
	}
}
