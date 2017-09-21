package hr.fer.zemris.java.gui.layouts;

/**
 * Represents a constraint for {@link CalcLayout} class. 
 * @author Ante
 *
 */
public class RCPosition {
	
	/**Row */
	private int row;
	
	/**Column*/
	private int column;

	/**
	 * Constructs an instance of this class using specified arguments
	 * @param row row to set
	 * @param column column to set
	 */
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	/**
	 * Constructs an instance of this class using specified argument. Argument
	 * must look like: {@code "x,y"}
	 * @param argument row to set
	 */
	public RCPosition(String argument) {
		if (argument == null) {
			throw new IllegalArgumentException("Argument must not be null");
		}
		parseArg(argument);
	}

	/**
	 * Parses given argument. 
	 * @param argument arg to parse
	 */
	private void parseArg(String argument) {
		String[] values = argument.split(",");
		try {
			row = Integer.parseInt(values[0]);
			column =  Integer.parseInt(values[1]);
		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Given argument is not properly"
					+ " formatted, was "+argument);
		}
	}
	
	/**
	 * Returns the row
	 * @return the row
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Returns the column
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}
	
	@Override
	public boolean equals(Object obj) {
		RCPosition pos = (RCPosition) obj;
		if (pos.column == this.column && pos.row == this.row) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(row) + Integer.hashCode(column);
	}
	
}
