package hr.fer.zemris.java.hw11.jnotepadapp.local;

/**
 * Represents a a decorator for some other {@link ILocalizationProvider}. Offers two additional 
 * methods for connecting and disconnecting a listener to {@link ILocalizationProvider}.
 * @author Ante Grbeša
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
	/**Listener registered*/
	private ILocalizationListener listener;	
	
	/**Parent localization provider*/
	private ILocalizationProvider parent;
	
	/**Current connection state*/
	private boolean connected;
	
	
	/**
	 * Constructs an instance of this class with specified {@link LocalizationProvider}.
	 * @param parent parent to set
	 */
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		super();
		this.parent = parent;
		this.connected = false;
	}
	
	/**
	 * Deregisters this object from decorated object. 
	 */
	public void disconnect() {
		if (connected) {
			parent.removeLocalizationListener(listener);
			connected = false;
		}
	}
	
	/**
	 * Registers this object to decorated object. 
	 */
	public void connect() {
		if (! connected) {
			listener = () -> fire();
			parent.addLocalizationListener(listener);
			connected = true;
		}
	}

	@Override
	public String getString(String key) {
		return parent.getString(key);
	}

}
