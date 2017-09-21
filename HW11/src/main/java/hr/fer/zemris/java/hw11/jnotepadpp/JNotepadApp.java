package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadapp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadapp.local.LJMenu;
import hr.fer.zemris.java.hw11.jnotepadapp.local.LJMenuItem;
import hr.fer.zemris.java.hw11.jnotepadapp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadapp.local.LocalizationProvider;

/**
 * Represents a simple text file editor created using Swing components. This editor is also 
 * internationalized and offers 3 languages (English, German, Croatian). 
 * @author Ante Grbeša
 *
 */
public class JNotepadApp extends JFrame {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**Tabbed pane in frame*/
	private JTabbedPane tabbedPane;
	
	/**Counter for tabs*/
	private int count = 0;
	
	/**Red icon, used when text is edited*/
	private Icon redIcon = createIcon("icons//red.png");
	
	/**Green icon, used when text is not edited*/
	private Icon greenIcon = createIcon("icons//green.png");
	
	/**Name of file viewed in current tab*/
	private String currentFile = "";
	
	/**Length label*/
	private JLabel lengthLab;
	
	/**Line label*/
	private JLabel lnLab;
	
	/**Column label*/
	private JLabel colLab;
	
	/**Selected text label*/
	private JLabel selLab;
	
	/**Timer, used for clock*/
	private Timer timer;
	
	/**Currently used language*/
	private String currentLang = "en";
	
	/**Toggable menu items*/
	private List<JMenuItem> toggable = new ArrayList<>();
	
	/**Localization provider*/
	private FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
	
	/**
	 * Runs once the program has started.
	 * @param args not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> { 
			new JNotepadApp().setVisible(true);
		});
	}
	
	/**
	 * Constructs an instance of this class and initializes GUI.
	 */
	public JNotepadApp() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		
		setSize(1024, 768);
		setLocationRelativeTo(null);

		
		initGUI();
		setTitle("New 1 - JNotepad++");
		setLocationRelativeTo(null);
	}

	/**
	 * Initializes GUI.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		createActions();
		
		createMenus();
		
		createToolbars();
		
		createStatusBar();
		
		createTabs();
		
		createStatusBar();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				checkAllTabs();
			}
		});
	}

	/**
	 * Creates and adds a status bar to the frame.
	 */
	private void createStatusBar() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 4));
		
		lengthLab = new JLabel("Length: 0");
		lengthLab.setForeground(Color.GRAY);
		lengthLab.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lengthLab);
		
		lnLab = new JLabel("Ln: 0");
		lnLab.setForeground(Color.GRAY);
		lnLab.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lnLab);
		
		colLab = new JLabel("Col: 1");
		colLab.setForeground(Color.GRAY);
		colLab.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(colLab);
		
	    selLab = new JLabel("Sel: 0");
		selLab.setForeground(Color.GRAY);
		selLab.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(selLab);
		
		JLabel clock = new JLabel(new Date().toString());
		clock.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(clock, BorderLayout.PAGE_END);
		timer = new Timer(100, (e) -> { 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			clock.setText(sdf.format(new Date()));
		});
		timer.start();
		
		getContentPane().add(panel, BorderLayout.PAGE_END);
	}

	/**
	 * Checks all opened documents for change and terminates program. 
	 */
	private void checkAllTabs() {
		boolean close = true;
		for (int i = 0, n = tabbedPane.getTabCount()-1; i <= n; i++) {
			if (! checkForSaving(i)) {
				close = false;
				break;
			}
		}
		
		if (close) {
			timer.stop();
			timer = null;
			dispose();
			System.exit(0);	//terminates timer 
		}
	}

	/**
	 * Creates and adds a tabbed pane to the frame.
	 */
	private void createTabs() {
		tabbedPane = new JTabbedPane();
		
		//changes title and status bar 
		tabbedPane.addChangeListener((l) -> { 
			int index = tabbedPane.getSelectedIndex();
			if (index < 0) {
				currentFile = "-";
			} else {
				currentFile = tabbedPane.getToolTipTextAt(index);
			}
			
			if (currentFile.isEmpty()) {	//file not yet saved
				currentFile = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
			}
			
			setTitle(currentFile + " - JNotepad++");
			
			
			JTextArea current = getSelectedTextArea();
			if (current == null) return;
			CaretListener[] listeners = current.getCaretListeners();
			listeners[0].caretUpdate(new CaretEvent(current) {
				
				/**
				 * Serial UID.
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public int getMark() {
					return 0;	//not used
				}
				
				@Override
				public int getDot() {
					return current.getCaretPosition();
				}
			});
		});
		
		createSingleTab(null, null);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);	
	}
	
	/**
	 * Creates and a single tab with specified arguments.
	 * @param text text for JTextArea
	 * @param path Path to file opened 
	 */
	private void createSingleTab(String text, Path path) {
		if (text == null) {
			text = "";
		}
		
		JTextArea txtArea = new JTextArea(text);
		txtArea.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {		
				changeIcon();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {		
				changeIcon();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				changeIcon();
			}
			
			private void changeIcon() {
				tabbedPane.setIconAt(tabbedPane.getSelectedIndex(), redIcon);
			}
		});
		txtArea.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent e) {
				lengthLab.setText("Length: "+txtArea.getText().length());
				
				int offset = txtArea.getCaretPosition();
	            int line= 1;
				try {
					line = txtArea.getLineOfOffset(offset);
				} catch (BadLocationException ignorable) {}
				lnLab.setText("Ln: "+(line+1));
				
				int col = 1;
				try {
					col = offset - txtArea.getLineStartOffset(line);
				} catch (BadLocationException ignorable) {}
				colLab.setText("Col: "+ (col+1));
				
				int sel = Math.abs(txtArea.getSelectionStart() - txtArea.getSelectionEnd());	
				selLab.setText("sel: "+sel);
				for (JMenuItem i : toggable) {
					boolean enabled;
					enabled = sel != 0;
					i.setEnabled(enabled);
				}
			}
		});
		
		JScrollPane scrPane = new JScrollPane(txtArea);
		if (path == null) {	//create empty tab	
			String name = "New " + (count +1);
			tabbedPane.addTab(name, greenIcon, scrPane, "");
			count++;
			return;
		}
		tabbedPane.addTab(path.getFileName().toString(), greenIcon, scrPane, path.toString());
		count++;
	}
	
	
	/**
	 * Creates an icon from specified path.
	 * @param path path of icon
	 * @return created ImageIcon
	 */
	private  ImageIcon createIcon(String path) {
		InputStream is = this.getClass().getResourceAsStream(path);
		if (is == null) {
			throw new IllegalArgumentException();
		}
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		int n;
		byte[] data = new byte[4096];
		try {
			while ((n = is.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, n);
			}
			is.close();
		} catch (IOException e) {
			throw new RuntimeException();
		}
		
		return new ImageIcon(buffer.toByteArray());
		
	}

	/**
	 * Adds some values to actions used. 
	 */
	private void createActions() {
		createBlankDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		createBlankDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		
		openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		
		saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		
		saveDocumentAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt S"));
		saveDocumentAsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		
		closeCurrentTabAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
		closeCurrentTabAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);

		copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);

		cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
	
		pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);

		getStatsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt I"));
		getStatsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
		
		toUpperCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control U"));
		toUpperCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		
		toLowerCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
		toLowerCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
		
		invertSelected.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
		invertSelected.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
		
		uniqueLinesAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
		uniqueLinesAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);
		
		sortAscAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt A"));
		sortAscAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		
		sortDescAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt D"));
		sortDescAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);

		exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F4"));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
	}
	
	/**
	 * Creates and adds menu bar to the frame.
	 */
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new LJMenu("file", flp);
		menuBar.add(fileMenu);
		
		fileMenu.add(new JMenuItem(createBlankDocument));
		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAsAction));
		fileMenu.add(new JMenuItem(closeCurrentTabAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(getStatsAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(exitAction));
		
		JMenu editMenu = new LJMenu("edit", flp);
		menuBar.add(editMenu);
		
		editMenu.add(new JMenuItem(copyAction));
		editMenu.add(new JMenuItem(pasteAction));
		editMenu.add(new JMenuItem(cutAction));
		
		JMenu langMenu = new LJMenu("lang", flp);
		JMenuItem hr = new LJMenuItem("cro", flp);
		hr.addActionListener((l) -> { 
			LocalizationProvider.getInstance().setLanguage("hr");
			currentLang = "hr";
		});
		
		langMenu.add(hr);
		JMenuItem en = new LJMenuItem("eng", flp);
		en.addActionListener((l) -> { 
			 LocalizationProvider.getInstance().setLanguage("en");
			 currentLang = "en";
		});
		langMenu.add(en);
		JMenuItem de =  new LJMenuItem("de", flp);
		de.addActionListener((l) -> { 
			 LocalizationProvider.getInstance().setLanguage("de");
			 currentLang = "de";
		});
		langMenu.add(de);
		menuBar.add(langMenu);
		
		JMenu toolsMenu = new LJMenu("tools", flp);
		JMenuItem toUp = new JMenuItem(toUpperCaseAction);
		toolsMenu.add(toUp);
		toggable.add(toUp);
		toUp.setEnabled(false);
		JMenuItem toLow = new JMenuItem(toLowerCaseAction);
		toolsMenu.add(toLow);
		toggable.add(toLow);
		toLow.setEnabled(false);
		JMenuItem inv = new JMenuItem(invertSelected);
		toolsMenu.add(inv);
		toggable.add(inv);
		inv.setEnabled(false);
		menuBar.add(toolsMenu);
		
		JMenu sort = new LJMenu("sort", flp);
		JMenuItem sortAsc = new JMenuItem(sortAscAction);
		sort.add(sortAsc);
		toggable.add(sortAsc);
		JMenuItem sortDesc = new JMenuItem(sortDescAction);
		sort.add(sortDesc);
		toggable.add(sortDesc);
		JMenuItem uniq = new JMenuItem(uniqueLinesAction);
		toolsMenu.add(uniq);
		toggable.add(uniq);
		
		toolsMenu.add(sort);
		setJMenuBar(menuBar);

	}
	
	/**
	 * Creates and adds a tool bar to the frame.
	 */
	private void createToolbars() {
		JToolBar toolBar = new JToolBar("Tool bar");
		toolBar.add(createBlankDocument);
		toolBar.add(openDocumentAction);
		toolBar.add(saveDocumentAction);
		toolBar.add(saveDocumentAsAction);
		toolBar.add(closeCurrentTabAction);
		
		toolBar.addSeparator();
		toolBar.add(copyAction);
		toolBar.add(cutAction);
		toolBar.add(pasteAction);
		
		toolBar.addSeparator();
		toolBar.add(getStatsAction);
		
		toolBar.addSeparator();
		toolBar.add(exitAction);
		
		getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}

	/**
	 * Returns currently viewed text area.
	 * @return currently viewed text area if possible, null otherwise.
	 */
	private JTextArea getSelectedTextArea() {
		int index = tabbedPane.getSelectedIndex();
		if (index == -1) {
			return null;
		}
		JScrollPane sp = (JScrollPane) tabbedPane.getComponent(index);
		JTextArea editor = (JTextArea) sp.getViewport().getView();
		
		return editor;
	}
	
	/**
	 * Changes icon of currently opened tab. 
	 * @param icon icon to set
	 */
	private void changeCurrentIcon(Icon icon) {
		tabbedPane.setIconAt(tabbedPane.getSelectedIndex(), icon);
	}
	
	
	
	// ----------------------------------------------------------------------------------------------------------
	// ---------------------What follow are implementations of actions (and methods they use)--------------------
	// --------------------------------------used in this application -------------------------------------------
	// ----------------------------------------------------------------------------------------------------------
	 
	
	/**
	 * Action for creation of a blank document.
	 */
	private final Action createBlankDocument = new LocalizableAction("openB", flp, "createD") {
		
		/**
		 * Serial UID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			createSingleTab(null, null);
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
		}
	};
	
	/**
	 * Opens a document. 
	 */
	private final Action openDocumentAction = new LocalizableAction("open", flp, "openD") {
		
		/**
		 * Serial UID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle(flp.getString("open"));
			if (fc.showOpenDialog(JNotepadApp.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			
			File file = fc.getSelectedFile();
			Path filePath = file.toPath();
			
			if (! Files.isReadable(filePath)) {
				JOptionPane.showMessageDialog(
						JNotepadApp.this,
						flp.getString("errorRead"),
						flp.getString("error"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			byte[] data = null;
			try {
				data = Files.readAllBytes(filePath);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(
						JNotepadApp.this,
						flp.getString("errorOpen")+" "+filePath+".",
						flp.getString("error"),
						JOptionPane.ERROR_MESSAGE);
				return; 
			}
			
			String text = new String(data, StandardCharsets.UTF_8);
			createSingleTab(text, filePath);
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
		}
	};
	
	/**
	 * Closes currently opened tab.
	 */
	private Action closeCurrentTabAction = new LocalizableAction("close", flp, "closeD") {
		
		/**
		 * Serial UID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			checkForSaving(null);
			
			int i = tabbedPane.getSelectedIndex();
			if (i != -1) {
				tabbedPane.remove(i);
				count--;
			}
	        if (count == 0) {
	        	createSingleTab(null, null);
	        }
			
		}
	};
	
	/**
	 * Checks tab at specified index for modifications.
	 * @param i index of wanted tab
	 * @return true if document is not modified or is saved, false otherwise
	 */
	private boolean checkForSaving(Integer i) {
		int index;
		if (i == null) {
			index = tabbedPane.getSelectedIndex();
		} else {
			index = i;
		}
		
		if (tabbedPane.getIconAt(index) == redIcon) {
			int pressed = JOptionPane.showConfirmDialog(
					JNotepadApp.this,
					tabbedPane.getTitleAt(index)+" "+flp.getString("notSaved"),
					flp.getString("save"),
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (pressed == JOptionPane.YES_OPTION) {
				saveDocumentAction.actionPerformed(null);
				return true;
			}
			if (pressed == JOptionPane.CANCEL_OPTION) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Saves text from currently viewed text area to specified path.
	 * @param file path of a file
	 */
	private void saveCurrentFile(String file) {
		JTextArea editor = getSelectedTextArea();
		Path path = Paths.get(file);
		
		try {
			Files.write(path, editor.getText().getBytes(StandardCharsets.UTF_8));
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(
					JNotepadApp.this,
					flp.getString("saveError"),
					flp.getString("error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(
				JNotepadApp.this,
				flp.getString("saved"),
				"Info",
				JOptionPane.INFORMATION_MESSAGE);
		
		changeCurrentIcon(greenIcon);
		
		int index = tabbedPane.getSelectedIndex();
		tabbedPane.setToolTipTextAt(index, path.toString());
		tabbedPane.setTitleAt(index, path.getFileName().toString());
		
		for (ChangeListener l : tabbedPane.getChangeListeners()) {
			l.stateChanged(new ChangeEvent(tabbedPane));
		}
	}
	
	/**
	 * Saves current document. 
	 */
	private Action saveDocumentAction = new LocalizableAction("save", flp, "saveD") {
		
		/**
		 * Serial UID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			String s = tabbedPane.getToolTipTextAt(tabbedPane.getSelectedIndex());
			Path path;
			if (s.isEmpty()) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle(flp.getString("save"));
				
				if (fc.showSaveDialog(JNotepadApp.this) != JFileChooser.APPROVE_OPTION) {
					return;
				}
				
				path = fc.getSelectedFile().toPath();
			} else {
				path = Paths.get(s);
			}

			saveCurrentFile(path.toString());
		}
	};
	
	/**
	 * Saves current document as a new document.
	 */
	private Action saveDocumentAsAction = new LocalizableAction("saveAs", flp, "saveAD") {
		
		/**
		 * Serial UID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle(flp.getString("save"));
			
			if (fc.showSaveDialog(JNotepadApp.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			
			Path path = fc.getSelectedFile().toPath();
			
			if (path.toFile().exists()) {
				int pressed = JOptionPane.showConfirmDialog(
						JNotepadApp.this,
						path.getFileName()+" "+flp.getString("overwrite"),
						flp.getString("over"),
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);
				if (pressed == JOptionPane.NO_OPTION) {
					return;
				}
			}
			
			saveCurrentFile(path.toString());

		}
	};
	
	/**
	 * Notifies user of statistical information for current text. 
	 */
	private Action getStatsAction = new LocalizableAction("getS", flp, "statsD") {
		
		/**
		 * Serial UID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea txtArea = getSelectedTextArea();
			String text = txtArea.getText();
			char[] data = text.toCharArray();
			int numOfChars = data.length;
			int numOfNonBlank = 0;
			int numOfLines = txtArea.getLineCount();
			
			for (char c : data) {
				if (! Character.isWhitespace(c)) numOfNonBlank++;
			}
			
			JOptionPane.showMessageDialog(JNotepadApp.this,
					flp.getString("contains")+" "+numOfChars+" "+flp.getString("char")+" "+numOfNonBlank
					+" "+flp.getString("blank")+" "+numOfLines+" "+flp.getString("line"));
			
		}
	};
	
	/**
	 * Copies selected text to clipboard.
	 */
	private Action copyAction = new LocalizableAction("copy", flp, "copyD") {
		
		/**
		 * Serial UID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea txtArea = getSelectedTextArea();
			txtArea.copy();
		}
	};
	
	/**
	 * Cuts selected text to clipboard.
	 */
	private Action cutAction = new LocalizableAction("cut", flp, "cutD") {

		/**
		 * Serial UID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea txtArea = getSelectedTextArea();
			txtArea.cut();
		}
	};
	
	/**
	 * Pastes text from clipboard to text area.
	 */
	private Action pasteAction = new LocalizableAction("paste", flp, "pasteD") {
		
		/**
		 * Serial UID.
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea txtArea = getSelectedTextArea();
			txtArea.paste();
		}
	};
	
	/**
	 * Applies given function to selected text. 
	 * @param func function to apply
	 */
	private void applyOnText(Function<String, String> func) {
		JTextArea editor = getSelectedTextArea();
		Document doc = editor.getDocument();
		
		int offset = 0;
		int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
		if (len == 0) {
			len = doc.getLength();
		} else {
			offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
		}

		try {
			String text = doc.getText(offset, len);
			text = func.apply(text);
			doc.remove(offset, len);
			doc.insertString(offset, text, null);
		} catch (BadLocationException ignorable) {
		}
	}
	
	/**
	 * Inverts selected text. 
	 */
	private final Action invertSelected = new LocalizableAction("inv", flp, "invD") {
		
		/**
		 * Serial UID.
		 */
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			applyOnText((s) -> invertText(s));
		}

		private String invertText(String text) {
			StringBuilder sb = new StringBuilder(text.length());
			for (char c : text.toCharArray()) {
				if (Character.isUpperCase(c)) {
					sb.append(Character.toLowerCase(c));
				} else if (Character.isLowerCase(c)) {
					sb.append(Character.toUpperCase(c));
				} else {
					sb.append(c);
				}
			}
			
			return sb.toString();
		}
	};
	
	/**
	 * Inverts selected text to lower case. 
	 */
	private final Action toLowerCaseAction = new LocalizableAction("low", flp, "lowD") {

		/**
		 * Serial UID.
		 */
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			applyOnText((s) -> toLower(s));
		}

		private String toLower(String text) {
			StringBuilder sb = new StringBuilder(text.length());
			for (char c : text.toCharArray()) {
				sb.append(Character.toLowerCase(c));
			}
			
			return sb.toString();
		}
	};
	
	/**
	 * Inverts selected text to upper case. 
	 */
	private final Action toUpperCaseAction = new LocalizableAction("up", flp, "upD") {

		/**
		 * Serial UID.
		 */
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			applyOnText((s) -> toUpper(s));
		}

		private String toUpper(String text) {
			StringBuilder sb = new StringBuilder(text.length());
			for (char c : text.toCharArray()) {
				sb.append(Character.toUpperCase(c));
			}
			
			return sb.toString();
		}
	};	
	
	/**
	 * Sorts text according to specified arguments. 
	 * @param text text to sort
	 * @param comp comparison to use, -1 for ascending, 1 for descending and
	 * 0 for duplicates
	 * @return sorted text
	 */
	private String sortText(String text, int comp) {
		List<String> lines = Arrays.asList(text.split("\\r?\\n"));
		
		Locale locale = new Locale(currentLang);
		Collator collator = Collator.getInstance(locale);
		if (comp < 0) {	//compare ascending
			Collections.sort(lines, collator);
		} else if (comp > 0) {	//compare descending
			Collections.sort(lines, collator.reversed());
		} else {	//remove duplicate
			Set<String> copy = new LinkedHashSet<>(lines);
			lines = new ArrayList<>();
			lines.addAll(copy);
			copy.clear();
		}
		
		String newLine = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		lines.forEach((s) -> { 
			sb.append(s);
			sb.append(newLine);
		});
		
		return sb.toString();
	}
	
	/**
	 * Applies given function to selected lines.
	 * @param func function to apply
	 */
	private void applyOnLines(Function<String, String> func) {
		JTextArea editor = getSelectedTextArea();
		Document doc = editor.getDocument();
		
		int offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
		int start = 0;
		int end = 0;
		int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());;
		try {
			int lineS = editor.getLineOfOffset(offset);
			int lineE = editor.getLineOfOffset(offset+len);
			start = editor.getLineStartOffset(lineS);
			end = editor.getLineEndOffset(lineE);
			len = end - start;
		} catch (BadLocationException ignorable) {
		}
		
		try {
			String text = doc.getText(start, len);
			text = func.apply(text);
			doc.remove(start, len);
			doc.insertString(start, text, null);
		} catch (BadLocationException ignorable) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * Sorts selected text in ascending order.
	 */
	private final Action sortAscAction = new LocalizableAction("asc", flp, "ascD") {

		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			applyOnLines((s) -> sortText(s, -1));
		}
	};	
	
	/**
	 * Sorts selected text in descending order.
	 */
	private final Action sortDescAction = new LocalizableAction("desc", flp, "descD") {
		/**
		 * Serial UID.
		 */
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			applyOnLines((s) -> sortText(s, 1));
		}
	};
	
	/**
	 * Removes duplicates from selected lines.
	 */
	private final Action uniqueLinesAction = new LocalizableAction("uniq", flp, "uniqD") {
		/**
		 * Serial UID.
		 */
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			applyOnLines((s) -> sortText(s, 0));
		}
	};
	
	/**
	 * Terminates the application.
	 */
	private final Action exitAction = new LocalizableAction("exit", flp, "exitD") {
		/**
		 * Serial UID.
		 */
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			checkAllTabs();
		}
	};


}
