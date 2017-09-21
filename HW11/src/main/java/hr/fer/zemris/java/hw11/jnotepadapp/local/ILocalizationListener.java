package hr.fer.zemris.java.hw11.jnotepadapp.local;

/**
 * Interface for all localization listeners. 
 * @author Ante Grbeša
 *
 */
public interface ILocalizationListener {

	/**
	 * This method is called once the selected language changes. 
	 */
	void localizationChanged();
}
