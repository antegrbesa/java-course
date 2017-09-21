package hr.fer.zemris.java.hw11.jnotepadapp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * This class is derived from {@link LocalizationProviderBridge};in its constructor it registers itself 
 * as a WindowListener to its JFrame  when frame is opened, it calls {@link #connect()} and when frame is closed,
 *  it calls {@link #disconnect()}.
.
 * @author Ante Grbeša
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge{
	

	/**
	 * Constructs an instance of this class and registers itself 
     * as a WindowListener to specified frame.
	 * @param provider localization provider
	 * @param frame frame to set
	 */
	public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
		super(provider);
		frame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}
			
		});
	}
	
	
	
}
