package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


/**
 * Demo class for {@link PrimListModel} class. 
 * @author Ante Grbesa
 *
 */
public class PrimDemo extends JFrame {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Main method
	 * @param args not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> { 
			new PrimDemo().setVisible(true);
		});

	}
	
	/**
	 * Constructor for this class. Also initializes GUI.
	 */
	public PrimDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		setLocation(20, 20);
		setSize(500, 200);
		setTitle("Prim demo");
		
		initGUI();
	}

	/**
	 * initializes GUI.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		PrimListModel model = new PrimListModel();
		
		JList<Integer> list1 = new JList<>(model);
		JScrollPane listPan1 = new JScrollPane(list1);

		
		JList<Integer> list2 = new JList<>(model);
		JScrollPane listPan2 = new JScrollPane(list2);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(listPan1);
		panel.add(listPan2);
		
		cp.add(panel, BorderLayout.CENTER);
		
		JButton button = new JButton("Next");
		cp.add(button, BorderLayout.PAGE_END);
		button.addActionListener((e) -> {
			model.next();
		});
		
		
		
	}

}
