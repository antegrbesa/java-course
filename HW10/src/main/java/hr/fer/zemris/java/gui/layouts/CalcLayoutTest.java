package hr.fer.zemris.java.gui.layouts;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;




/**
 * A simple test class for {@link CalcLayout}.
 * @author Ante
 *
 */
public class CalcLayoutTest extends JFrame {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public CalcLayoutTest() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		setLocation(20, 20);
		setSize(500, 200);
		setTitle("Test");
		
		initGUI();
		pack();
	}
	
	/**
	 * Initializes GUI.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(3));
		
		//for other part of the test, remove comment tag from following code
		
//		JLabel l  = new JLabel("x");
//		l.setBorder(BorderFactory.createLineBorder(Color.black));
//		JLabel l2  = new JLabel("x");
//		l2.setBorder(BorderFactory.createLineBorder(Color.black));
//		JLabel l3  = new JLabel("x");
//		l3.setBorder(BorderFactory.createLineBorder(Color.black));
//		JLabel l4  = new JLabel("x");
//		l4.setBorder(BorderFactory.createLineBorder(Color.black));
//		JLabel l5  = new JLabel("x");
//		l5.setBorder(BorderFactory.createLineBorder(Color.black));
//		JLabel l6  = new JLabel("x");
//		l6.setBorder(BorderFactory.createLineBorder(Color.black));
//		cp.add(l, new RCPosition(1,1));
//		cp.add(l2, new RCPosition(2,3));
//		cp.add(l3, new RCPosition(2,7));
//		cp.add(l4, new RCPosition(4,2));
//		cp.add(l5, new RCPosition(4,5));
//		cp.add(l6, new RCPosition(4,7));
//		cp.setMinimumSize(getContentPane().getMinimumSize());
		
		for (int i = 1; i <= 5; i++) {
			for (int j = 1; j <= 7; j++) {
				JButton button = new JButton(i + ", " + j);
				try {
					cp.add(button, new RCPosition(i, j));
				} catch (IllegalArgumentException e) {
					continue;
				}
			}
		}
		
	}
	
	/**
	 * Main method.
	 * @param args not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new CalcLayoutTest().setVisible(true);
		});
	}

}
