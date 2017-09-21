package hr.fer.zemris.java.hw11.jnotepadapp.local;

import java.util.ArrayList;
import java.util.List;

/**
 *  Implements ILocalizationProvider interface and adds it the ability to register,
 *   de-register and inform listeners using {@link #fire()} method.
 * @author Ante
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

	/**List of listeners*/
	private List<ILocalizationListener> listeners;
	
	/**
	 * Constructor for this class. 
	 */
	public AbstractLocalizationProvider() {
		listeners = new ArrayList<>();
	}
	
	@Override
	public void addLocalizationListener(ILocalizationListener e) {
		listeners = new ArrayList<>(listeners);
		listeners.add(e);
	}
	
	@Override
	public void removeLocalizationListener(ILocalizationListener e) {
		listeners = new ArrayList<>(listeners);
		listeners.remove(e);
	}
	
	/**
	 * Informs all registered listeners. 
	 */
	public void fire() {
		for (ILocalizationListener l : listeners) {
			l.localizationChanged();
		}
	}
	
}
