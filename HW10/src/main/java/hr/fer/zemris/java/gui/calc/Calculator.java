package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;


import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * A simple calculator with GUI. Most of the operations are normal operations from
 * classic calculators except for {@code push} and {@code pop}. These operations work with a stack
 * and are used for storing a value and getting it. Also, operation {@code inv} inverts all invertible operations. 
 * @author Ante Grbesa
 *
 */
public class Calculator extends JFrame {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Last displayed number.
	 */
	private String currentDisplay = "0";
	
	/**Current operation to perform next*/
	private BinaryActionListener currentOperation;
	
	/**Flag for presence of an error*/
	private boolean error = false;
	
	/**Text field of this calculator*/
	private JTextField txtField;
	
	/**Stack for this calculator*/
	private Stack<String> stack = new Stack<>();
	
	/**Invertable buttons*/
	private Map<JButton ,InvertibleListener> invertable = new HashMap<>();
	
	/**
	 * Main method
	 * @param args not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Calculator().setVisible(true));
	}
	
	/**
	 * Constructor for this class. Also initializes GUI.
	 */
	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		setLocation(20, 20);
		setSize(500, 200);
		setTitle("Calculator");
		setMinimumSize(getContentPane().getMinimumSize());
		
		initGUI();
	

	}
	
	/**
	 * Method for initialization of GUI for this calculator. 
	 */
	private void initGUI() {
		Container cp = getContentPane();
		JPanel panel = new JPanel(new CalcLayout(3));
		
		txtField = new JTextField(currentDisplay);
		txtField.setEditable(false);
		txtField.setBackground(Color.YELLOW);
		txtField.setHorizontalAlignment(JTextField.RIGHT);
		panel.add(txtField, new RCPosition(1, 1));
		
		addNumericButtons(panel);
		
		JButton btnDot = new JButton(".");
		btnDot.addActionListener(new NumberActionListener("."));
		panel.add(btnDot, new RCPosition(5, 5));
		
		JButton btnDivide = new JButton("/");
		btnDivide.addActionListener(new BinaryActionListener("/", null, false, (r,s) -> r/s, null));
		panel.add(btnDivide, new RCPosition(2, 6));
		JButton btnMul = new JButton("*");
		btnMul.addActionListener(new BinaryActionListener("*", null, false, (r,s) -> r*s, null));
		panel.add(btnMul, new RCPosition(3, 6));
		JButton btnMinus = new JButton("-");
		btnMinus.addActionListener(new BinaryActionListener("-", null, false, (r,s) -> r-s, null));
		panel.add(btnMinus, new RCPosition(4, 6));
		JButton btnPlus = new JButton("+");
		btnPlus.addActionListener(new BinaryActionListener("+", null, false, (r,s) -> r+s, null));
		panel.add(btnPlus, new RCPosition(5, 6));
		
		JButton btnEquals = new JButton("=");
		btnEquals.addActionListener((e) -> {
			if (currentOperation == null) {
				return;
			}
			Double secondArg;
			try {
				secondArg = Double.parseDouble(currentDisplay);
			} catch (NumberFormatException exc) {
				writeError("Error, press res for reset.");
				return;
			}
			
			Double result = currentOperation.getOperation().applyAsDouble(currentOperation.firstArg, secondArg);
			currentDisplay = result.toString();
			txtField.setText(currentDisplay);
			currentOperation = null;
		});
		panel.add(btnEquals, new RCPosition(1, 6));
		
		//sign toggle button
		JButton btnToggle = new JButton("+/-");
		btnToggle.addActionListener((l) -> { 
			String writeText = txtField.getText();
			boolean singleNumber = false;
			if (currentDisplay.equals(writeText)) {
				singleNumber = true;
			}
			int diff = 0;
			if (currentDisplay.startsWith("-")) {	//if it's negative
				currentDisplay = currentDisplay.replaceFirst("-", "");
				diff = writeText.length() - currentDisplay.length()-1;
				
			} else {	//it's positive
				currentDisplay = "-" + currentDisplay.trim();
				diff = writeText.length() - currentDisplay.length() +1;
			}
			
			if (! singleNumber)  {	//more than one number present
				txtField.setText(writeText.substring(0, diff) + currentDisplay);
			} else {
				txtField.setText(currentDisplay);
			}
		});
		panel.add(btnToggle, new RCPosition(5, 4));
		
		JButton btnDivUnary = new JButton("1/x");
		btnDivUnary.addActionListener(new UnaryActionListener("1/x", null, false, (r)-> 1/r, null));
		panel.add(btnDivUnary, new RCPosition(2, 1));
		
		//log button
		JButton btnLog= new JButton("log");
		UnaryActionListener listenerLog = new UnaryActionListener("log", "10^", true, (r)-> Math.log10(r), (r) -> Math.pow(r, 10));
		btnLog.addActionListener(listenerLog);
		invertable.put(btnLog, listenerLog);
		panel.add(btnLog, new RCPosition(3, 1));
		
		//ln button
		JButton btnLn = new JButton("ln");
		UnaryActionListener listenerLn = new UnaryActionListener("ln", "e^", true, (r)-> Math.log(r), (r) -> Math.exp(r));
		btnLn.addActionListener(listenerLn);
		invertable.put(btnLn, listenerLn);
		panel.add(btnLn, new RCPosition(4, 1));
		
		//x^n  button
		JButton btnXn = new JButton("x^n");
		BinaryActionListener listenerXn = new BinaryActionListener("^", " root ", true, (r, s)-> Math.pow(r, s), (r,s) -> Math.pow(r, 1/s));
		btnXn.addActionListener(listenerXn);
		invertable.put(btnXn, listenerXn);
		panel.add(btnXn, new RCPosition(5, 1));
		
		//sin button
		JButton btnSin = new JButton("sin");
		UnaryActionListener listenerSin = new UnaryActionListener("sin", "arcsin", true, (r)-> Math.sin(r), (r) -> Math.asin(r));
		btnSin.addActionListener(listenerSin);
		invertable.put(btnSin, listenerSin);
		panel.add(btnSin, new RCPosition(2, 2));
		
		//cos button
		JButton btnCos = new JButton("cos");
		UnaryActionListener listenerCos = new UnaryActionListener("cos", "arccos", true, (r)-> Math.cos(r), (r) -> Math.acos(r));
		btnCos.addActionListener(listenerCos);
		invertable.put(btnCos, listenerCos);
		panel.add(btnCos, new RCPosition(3, 2));
		
		//tan button
		JButton btnTan = new JButton("tan");
		UnaryActionListener listenerTan = new UnaryActionListener("tan", "arctan", true, (r)-> Math.tan(r), (r) -> Math.atan(r));
		btnTan.addActionListener(listenerTan);
		invertable.put(btnTan, listenerTan);
		panel.add(btnTan, new RCPosition(4, 2));
		
		//ctg button
		JButton btnCtg = new JButton("ctg");
		UnaryActionListener listenerCtg = new UnaryActionListener("ctg", "arcctg", true, (r)-> 1/Math.tan(r), (r) -> Math.PI/2 -  Math.atan(r));
		btnCtg.addActionListener(listenerCtg);
		invertable.put(btnCtg, listenerCtg);
		panel.add(btnCtg, new RCPosition(5, 2));
		
		//button clear
		JButton btnClr = new JButton("clr");
		btnClr.addActionListener((l) -> { 
			
			replaceCurrent("");
		});
		panel.add(btnClr, new RCPosition(1, 7));
		
		//button reset
		JButton btnRes = new JButton("res");
		btnRes.addActionListener((l) -> { 
					
			txtField.setText("0");
			currentDisplay = "0";
			currentOperation = null;
		});
		panel.add(btnRes, new RCPosition(2, 7));
		
		//button push
		JButton btnPush = new JButton("push");
		btnPush.addActionListener((l) -> { 		
			stack.push(currentDisplay);
		});
		panel.add(btnPush, new RCPosition(3, 7));
		
		//button pop
		JButton btnPop = new JButton("pop");
		btnPop.addActionListener((l) -> { 	
			String replacement;
			try {
				replacement = stack.pop();
			} catch (EmptyStackException ex) {
				writeError("Stack empty");
				return;
			}
			
			replaceCurrent(replacement);
		});
		panel.add(btnPop, new RCPosition(4, 7));
		
		JCheckBox chkBox = new JCheckBox("inv");
		chkBox.addActionListener((l) -> { 
			for (Map.Entry<JButton, InvertibleListener> listen : invertable.entrySet()) {
				listen.getValue().invert();
				listen.getKey().setName(listen.getValue().getName());
			}
		});
		panel.add(chkBox, new RCPosition(5, 7));
		
		cp.add(panel);
		
	}
	
	/**
	 * Adds numerical buttons
	 * @param panel panel for buttons
	 */
	private void addNumericButtons(JPanel panel) {
		//add numeric buttons
		JButton btn0 = new JButton("0");
		btn0.addActionListener(new NumberActionListener("0"));
		panel.add(btn0, new RCPosition(5, 3));
		JButton btn1 = new JButton("1");
		btn1.addActionListener(new NumberActionListener("1"));
		panel.add(btn1, new RCPosition(4, 3));
		JButton btn2 = new JButton("2");
		btn2.addActionListener(new NumberActionListener("2"));
		panel.add(btn2, new RCPosition(4, 4));
		JButton btn3 = new JButton("3");
		btn3.addActionListener(new NumberActionListener("3"));
		panel.add(btn3, new RCPosition(4, 5));
		JButton btn4 = new JButton("4");
		btn4.addActionListener(new NumberActionListener("4"));
		panel.add(btn4, new RCPosition(3, 3));
		JButton btn5 = new JButton("5");
		btn5.addActionListener(new NumberActionListener("5"));
		panel.add(btn5, new RCPosition(3, 4));
		JButton btn6 = new JButton("6");
		btn6.addActionListener(new NumberActionListener("6"));
		panel.add(btn6, new RCPosition(3, 5));
		JButton btn7 = new JButton("7");
		btn7.addActionListener(new NumberActionListener("7"));
		panel.add(btn7, new RCPosition(2, 3));
		JButton btn8 = new JButton("8");
		btn8.addActionListener(new NumberActionListener("8"));
		panel.add(btn8, new RCPosition(2, 4));
		JButton btn9 = new JButton("9");
		btn9.addActionListener(new NumberActionListener("9"));
		panel.add(btn9, new RCPosition(2, 5));
		
	}
	
	/**
	 * Replace currently displayed number with specified one
	 * @param repl replacement number
	 */
	private void replaceCurrent(String repl) {
		String expr = txtField.getText();
		String first = expr.substring(0, expr.length() - currentDisplay.length());
		currentDisplay = repl;
		txtField.setText(first+repl);
	}
	
	/**
	 * Writes given error message to calculator.
	 * @param message message to write
	 */
	private void writeError(String message) {
		txtField.setText(message);
		currentDisplay = "0";
		currentOperation = null;
		error = true;
	}
	
	/**
	 * Implements a listener for numerical buttons.
	 * @author Ante Grbesa
	 *
	 */
	private class NumberActionListener implements ActionListener {

		/**Name of this listener*/
		private String name;
		
		/**
		 * Creates this listener with specified name
		 * @param name name to set
		 */
		public NumberActionListener(String name) {
			this.name = name;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (currentDisplay.equals("0")) {
				currentDisplay = "";
				txtField.setText("");
			}
			txtField.setText((error ? "" : txtField.getText()) + name);
			if (error) 
				error = false;
			
			currentDisplay += name;
		}	
	}
	
	/**
	 * Implements a listener for two-argument operator buttons like +,-,* etc. 
	 * @author Ante Grbesa
	 *
	 */
	private class BinaryActionListener implements ActionListener, InvertibleListener {
		/**Name of this listener*/
		private String name;
		
		/**Name of inverted operation*/
		private String invName;
		
		/**Is invertible*/
		private boolean invertable;
		
		/**First argument for operation*/
		private Double firstArg;
		
		/**Operation to perform*/
		private DoubleBinaryOperator operation;
		
		/**Inverted operation*/
		private DoubleBinaryOperator invOperation;
		
		/**
		 * Creates instance of this listener.
		 * @param name Name of this listener
		 * @param invName Name of inverted operation
		 * @param invertable Is invertible
		 * @param operation First argument for operation
		 * @param invOperation  Inverted operation
		 */
		public BinaryActionListener(String name, String invName, boolean invertable, DoubleBinaryOperator operation,
				DoubleBinaryOperator invOperation) {
			this.name = name;
			this.invName = invName;
			this.invertable = invertable;
			this.operation = operation;
			this.invOperation = invOperation;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			performOperation();
		}
		
		/**
		 * Performs current operation. 
		 */
		public void performOperation() {
			if (currentOperation == null) {	//no operation present
				try {
					firstArg = Double.parseDouble(currentDisplay);
				} catch (NumberFormatException ex) {
					writeError("Error, press re for reset.");
					return;
				}
				
				currentOperation = this;
				txtField.setText(currentDisplay + name);
				currentDisplay = "";
				return;
			}
			if (currentDisplay.isEmpty()) {		//last pressed button was operation, change old operation with this one
				String text = txtField.getText();
				txtField.setText(text.substring(0, text.length()-1) + name);	//remove last operation symbol
				firstArg = currentOperation.firstArg;
				currentOperation = this;
				return;
			}
			
			//perform previous operation and save result
			Double secondArg;
			try {
				secondArg = Double.parseDouble(currentDisplay);
			} catch (NumberFormatException exc) {
				writeError("Error, press re for reset.");
				return;
			}
			
			Double result;
			try {
				result = currentOperation.getOperation().applyAsDouble(currentOperation.firstArg, secondArg);
			} catch (Exception ex) {
				writeError("Math error");
				return;
			}
			
			currentDisplay = "";
			txtField.setText(result.toString()+name);
			firstArg = result;
			currentOperation = this;
		}
		
		/**
		 * Gets current operation.
		 * @return current operation
		 */
		public DoubleBinaryOperator getOperation() {
			return operation;
		}
		
		public void invert() {
			if (! invertable) {
				return;
			}
			
			DoubleBinaryOperator tmp = operation;
			operation = invOperation;
			invOperation = tmp;

			String nameTmp = name;
			name = invName;
			invName = nameTmp;		
		}

		@Override
		public String getName() {
			return name;
		}	
	}
	
	/**
	 * Implements a listener for single-argument operator buttons like sin,cos... etc. 
	 * @author Ante Grbesa
	 *
	 */
	private class UnaryActionListener implements ActionListener, InvertibleListener {

		/**Name of this listener*/
		private String name;
		
		/**Name of inverted operation*/
		private String invName;
		
		/**Is invertible*/
		private boolean invertable;
		
		/**Operation to perform*/
		private DoubleUnaryOperator operation;
		
		/**Inverted operation*/
		private DoubleUnaryOperator invOperation;
		
		/**
		 * Constructs this class with specified arguments.
		 * @param name Name of this listener
		 * @param invName Name of inverted operation
		 * @param invertable Is invertible
		 * @param operation First argument for operation
		 * @param invOperation  Inverted operation
		 */
		public UnaryActionListener(String name, String invName, boolean invertable, DoubleUnaryOperator operation,
				DoubleUnaryOperator invOperation) {
			super();
			this.name = name;
			this.invertable = invertable;
			this.operation = operation;
			this.invOperation = invOperation;
			this.invName = invName;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Double number;
			boolean second = false;
			if (currentOperation != null) {
				if (currentDisplay.isEmpty()) {
					number = currentOperation.firstArg;
					currentOperation = null;
				} else {
					try {
						number = Double.parseDouble(currentDisplay);
						second = true;
					} catch (NumberFormatException ex) {
						writeError("Invalid input");
						return;
					}
				}
			} else {
				try {
					number = Double.parseDouble(txtField.getText());
				} catch (NumberFormatException ex) {
					writeError("Invalid input");
					return;
				}
			}
			

			Double result;
			try {
				result = operation.applyAsDouble(number);
			} catch (Exception ex) {
				writeError("Math error");
				return;
			}
			
			String firstPart = "";
			if (second) {	//if this operator was performed on second argument of binary operator
				String expr = txtField.getText();
				firstPart = expr.substring(0, expr.length() - currentDisplay.length());
			}
			
			currentDisplay = result.toString();
			txtField.setText(firstPart + currentDisplay);
		}
		
		@Override
		public void invert() {
			if (! invertable) {
				return;
			}
			
			DoubleUnaryOperator tmp = operation;
			operation = invOperation;
			invOperation = tmp;

			String nameTmp = name;
			name = invName;
			invName = nameTmp;	
			
		}

		@Override
		public String getName() {
			return name;
		}
		
		
	}
}
