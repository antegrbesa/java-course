package hr.fer.zemris.java.hw16.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.hw16.factories.CircleFactory;
import hr.fer.zemris.java.hw16.factories.FilledCircleFactory;
import hr.fer.zemris.java.hw16.factories.LineFactory;
import hr.fer.zemris.java.hw16.interfaces.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.model.Rectangle;

/**
 * Main class that initializes GUI.
 * @author Ante
 *
 */
public class JVDraw extends JFrame {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 3864156777948534492L;

	/**
	 * Foreground color. 
	 */
	private JColorArea foregroundColor;

	/**
	 * Background color. 
	 */
	private JColorArea backgroundColor;

	/**
	 * Drawing canvas.
	 */
	private JDrawingCanvas canvas;

	/**
	 * Drawing model.
	 */
	private DrawingModel model;
	
	/**
	 * Default foreground color.
	 */
	private static final Color DEFAULT_FOREGROUND = Color.RED;
	
	/**
	 * Default background color. 
	 */
	private static final Color DEFAULT_BACKGROUND = Color.BLUE;

	/**
	 * JList that shows objects from canvas. 
	 */
	private JList<GeometricalObject> objectList;

	/**
	 * List model.
	 */
	private DrawingObjectListModel listModel;

	/**
	 * JList scroll pane.
	 */
	private JScrollPane scrollPane;

	/**
	 * File to save model at. 
	 */
	private File saveFile;


	/**
	 * Constructor that initializes GUI.
	 */
	public JVDraw() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(1024, 768);
		setTitle("JVDraw");
		initGUI();
	}

	/**
	 * Initializes GUI.
	 */
	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());

		addMenuBar();
		addToolbar();

		addCanvas();
		addList();

		addBottomComponent();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (canvas.isCanvasChanged() == true) {
					if (!saveFileDialog()) return;
				}
				
				dispose();
			}
		});
	}
	
	/**
	 * Shows a save dialog.
	 * @return false if cancel is selected, true otherwise.
	 */
	private boolean saveFileDialog() {
		int selection = 
				JOptionPane.showConfirmDialog(this, "Save current file?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION);
		if (selection == JOptionPane.CANCEL_OPTION) return false;
		if (selection == JOptionPane.YES_OPTION) {
			showSaveDialog(saveFile != null, "Save before exiting");
		}
		return true;
	}

	/**
	 * Adds list  to GUI.
	 */
	private void addList() {
		listModel = new DrawingObjectListModel(model);
		model.addDrawingModelListener(listModel);
		objectList = new JList<GeometricalObject>(listModel);

		objectList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = objectList.getSelectedIndex();
					model.updateObject(index);
					objectList.clearSelection();
				}
			}
		});

		scrollPane = new JScrollPane(objectList);
		scrollPane.setPreferredSize(new Dimension(180, 500));
		getContentPane().add(scrollPane, BorderLayout.LINE_END);
	}

	/**
	 * Adds canvas to GUI.
	 */
	private void addCanvas() {
		model = new DrawingModelImpl();
		canvas = new JDrawingCanvas(foregroundColor, backgroundColor, model);
		model.addDrawingModelListener(canvas);
		getContentPane().add(canvas, BorderLayout.CENTER);
	}

	/**
	 * Adds bottom label to GUI.
	 */
	private void addBottomComponent() {
		JPanel panel = new JPanel();
		JColorLabel label = new JColorLabel(foregroundColor, backgroundColor);
		backgroundColor.addColorChangeListener(label);
		foregroundColor.addColorChangeListener(label);
		panel.add(label);
		getContentPane().add(panel, BorderLayout.PAGE_END);
	}

	/**
	 * Adds toolbar to GUI.
	 */
	private void addToolbar() {
		JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
		toolbar.setFloatable(false);
		toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));

		foregroundColor = new JColorArea(DEFAULT_FOREGROUND);
		backgroundColor = new JColorArea(DEFAULT_BACKGROUND);

		toolbar.add(foregroundColor);
		toolbar.add(backgroundColor);

		ButtonGroup btGroup = new ButtonGroup();
		JToggleButton btLine = new JToggleButton("Line");
		btLine.addActionListener((e) -> canvas.setCommand(new LineFactory()));

		JToggleButton btCircle = new JToggleButton("Circle");
		btCircle.addActionListener((e) -> canvas.setCommand(new CircleFactory()));

		JToggleButton btFilledCircle = new JToggleButton("Filled circle");
		btFilledCircle.addActionListener((e) -> canvas.setCommand(new FilledCircleFactory()));

		btGroup.add(btLine);
		btGroup.add(btCircle);
		btGroup.add(btFilledCircle);

		toolbar.addSeparator();
		toolbar.add(btLine);
		toolbar.add(btCircle);
		toolbar.add(btFilledCircle);

		getContentPane().add(toolbar, BorderLayout.PAGE_START);
	}

	/**
	 * Adds menus to GUI. 
	 */
	private void addMenuBar() {
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		open.addActionListener((e) -> { 
			if (canvas.isCanvasChanged()) {
				showSaveDialog(saveFile != null,
						"Do you want to save current model before new model is opened?");
			}
			FileUtil.openFile(model);
			saveFile = null;
			canvas.setCanvasChanged(false);
		});
		menu.add(open);

		JMenuItem save = new JMenuItem("Save");
		save.addActionListener((e) -> {
			if (saveFile == null) {
				saveFile = FileUtil.getSaveFile();
			}
			if (saveFile == null) { return; }
			FileUtil.saveModelToFile(saveFile, model);
			canvas.setCanvasChanged(false);
		});
		menu.add(save);

		JMenuItem saveAs = new JMenuItem("Save As");
		saveAs.addActionListener((e) -> { 
			File saveLocation = FileUtil.getSaveFile();
			if (saveLocation == null) return; 
			FileUtil.saveModelToFile(saveLocation, model);
			canvas.setCanvasChanged(false);
		});
		menu.add(saveAs);

		JMenuItem export = new JMenuItem("Export");
		export.addActionListener((e) -> FileUtil.exportModel
				(model, new Rectangle(canvas.getWidth(), canvas.getHeight(), 0, 0)));
		menu.add(export);

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener((e) -> {
			if (canvas.isCanvasChanged() == true) {
				if (!saveFileDialog()) return;
			}
			this.dispose();
		});
		menu.add(exit);

		bar.add(menu);
		setJMenuBar(bar);
	}

	/**
	 * Opens a save dialog. 
	 * @param alreadySaved if model is already saved
	 * @param text text to show in dialog. 
	 */
	public void showSaveDialog(boolean alreadySaved, String text) {
		if (alreadySaved) {
			int status = JOptionPane.showConfirmDialog(this, text);
			if (status == JOptionPane.OK_OPTION) {
				FileUtil.saveModelToFile(saveFile, model);
			}
			return;
		}
		saveFile = FileUtil.getSaveFile();
		if (saveFile != null) {
			FileUtil.saveModelToFile(saveFile, model);
		}
	}

	/**
	 * Called once the program has started.
	 * @param args not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new JVDraw().setVisible(true);
			}
		});
	}
}
