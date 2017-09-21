package hr.fer.zemris.java.hw11.jnotepadapp.local;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Custom abstract class derived from {@link AbstractAction}. Uses localization
 * provider for name and description. 
 * @author Ante Grbeša
 *
 */
public abstract class LocalizableAction extends AbstractAction{

	/**
	 * Serial UId.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Localization provider
	 */
	private ILocalizationProvider provider;
	
	/**Key for action name*/
	private String key;
	
	/**Key for action description*/
	private String desc;

	/**
	 * Constructor for this class. Adds itself as a listener to given localization provider.
	 * @param key key for this class' name
	 * @param provider provider for translation
	 * @param desc Key for action description
	 */
	public LocalizableAction(String key, ILocalizationProvider provider, String desc) {
		super();
		this.provider = provider;
		this.key = key;
		this.desc = desc;
		updateValues();
		provider.addLocalizationListener(() -> { 
			updateValues();
		});
	}
	
	/**
	 * Updates name and short description.
	 */
	private void updateValues() {
		putValue(NAME, provider.getString(key));
		putValue(Action.SHORT_DESCRIPTION, provider.getString(desc));
	}
}
