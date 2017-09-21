package hr.fer.zemris.java.custom.scripting.exec;

import java.text.ParseException;
import java.util.function.BiFunction;

/**
 * Represents a wrapper for any type of object. Also offers methods for arithmetic operations
 * if currently stored object can participate in those operations (must be an instance of {@code Double},
 * {@code Integer} or {@code String}). If currently stored value is null, it is treated as a {@code Integer} 
 * with value of 0. 
 * @author Ante
 *
 */
public class ValueWrapper {
	
	/**Object that this class wraps*/
	private Object value;

	/**
	 * Creates an instance of this class with specified value to wrap. 
	 * @param value value to wrap
	 */
	public ValueWrapper(Object value) {
		super();
		this.value = value;
	}

	/**
	 * Returns currently stored value.
	 * @return currently stored value
	 *
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * Sets the value to the specified one.
	 * @param value value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * Checks if given element is legal for arithmetic operations. If given
	 * value is string, method tries to parse it to a matching number class and
	 * if given argument is null, returns integer value of 0.
	 * @param value value to check
	 * @return parsed value, or given value if value wasn't string or null
	 */
	private Object checkValue(Object value) {
		if(!(value instanceof Integer || value instanceof Double
				|| value instanceof String || value == null)) {
			throw new RuntimeException("Constructor argument type was invalid.");
		}
		if(value == null) {
			return 0;
		}
		if(value instanceof String) {
			try {
				return getStringValue(value);
			} catch (ParseException e) {
				throw new IllegalArgumentException("Invalid argument");
			}
		}
		
		return value;
	}
	
	/**
	 * Parses given value to matching number class (Double or Integer).
	 * @param value value to parse
	 * @return parsed value
	 * @throws ParseException if given string is not a number
	 */
	private Number getStringValue(Object value) throws ParseException {
		if(((String) value).contains(".") || ((String) value).contains("E")) {
				return Double.parseDouble((String) value);
			} else {
				return Integer.parseInt((String) value);
			}
		
	}
	
	/**
	 * Universal method that checks given objects and converts them to doubles and executes given arithmetic operation. 
	 * If any of given arguments were double, returned value is also double. Otherwise, returns integer value. 
	 * @param first first argument for operation
	 * @param second second argument for operation
	 * @param operation operation to perform
	 */
	private void doOperation(Object first, Object second, BiFunction<Double, Double, Double> operation) {
		first = checkValue(first);
		second = checkValue(second);
		
		boolean firstInt = false, secondInt = false;
		double tempFirst = 0.0, tempSecond = 0.0;
		if(first instanceof Integer) {
			tempFirst = (int) first;
			firstInt = true;
		} else {
			tempFirst = (double) first;
		}
		if(second instanceof Integer) {
			tempSecond = (int) second;
			secondInt = true;
		} else {
			tempSecond = (double) second;
		}
		
		Double result = operation.apply(tempFirst, tempSecond);
		if(firstInt && secondInt) {
			this.value = result.intValue();
		} else {
			this.value = result;
		}
	}
	
	/**
	 * Adds specified value to value that is stored in this class.
	 * @param incValue value to add
	 * @throws IllegalArgumentException if specified value or stored value 
	 * are illegal for this operation (i.e. not numbers or string representation of a number)
	 */
	public void add(Object incValue) {
		doOperation(this.value, incValue, (t1, t2) -> t1+t2);	
	}
	
	/**
	 * Subtracts specified value from value that is stored in this class.
	 * @param decValue value to value to subtract
	 * @throws IllegalArgumentException if specified value or stored value 
	 * are illegal for this operation (i.e. not numbers or string representation of a number)
	 */
	public void subtract(Object decValue) {	
		doOperation(this.value, decValue, (t1, t2) -> t1-t2);
	}
	
	/**
	 * Multiplies specified value with value that is stored in this class.
	 * @param mulValue value to multiply
	 * @throws IllegalArgumentException if specified value or stored value 
	 * are illegal for this operation (i.e. not numbers or string representation of a number)
	 */
	public void multiply(Object mulValue) {
		doOperation(this.value, mulValue, (t1, t2) -> t1*t2);
	}
	
	/**
	 * Divides currently stored value with specified value.
	 * @param divValue value for division
	 * @throws IllegalArgumentException if specified value or stored value 
	 * are illegal for this operation (i.e. not numbers or string representation of a number)
	 *  @throws ArithmeticException if specified value  and stored arguments are integers and
	 *  specified value is 0 (division by zero) 
	 */
	public void divide(Object divValue) {
		if(divValue == null) {
			throw new ArithmeticException("Division with zero.");
		}
		if(divValue instanceof Integer) {
			if((int) divValue == 0) {
				throw new ArithmeticException("Division with zero.");
			}
		}
		
		doOperation(this.value, divValue, (t1, t2) -> t1/t2);
	}
	
	/**
	 * Compares given value with stored value. Comparison is based on numerical value therefore if 
	 * any of arguments are not numbers, exception is thrown. 
	 * @param withValue value for comparison
	 * @return returns an integer less than zero if currently stored value is smaller than argument, an
	 *  integer greaterthan zero if currently stored value is larger than argument or an integer 0
	 *   if they are equal.
	 *  @throws IllegalArgumentException if specified value or stored value are illegal for this operation
	 *  (i.e. not numbers of string representations of a number)
	 */
	public int numCompare(Object withValue) {
		Object tmp = this.value;
		
		doOperation(this.value, withValue, new BiFunction<Double, Double, Double>() {
			@Override
			public Double apply(Double first, Double second) {
				int n = Double.compare(first, second);
				if(n < 0) {
					return -1.0;
				}
				if(n > 0) {
					return 1.0;
				}
				
				return 0.0;
			}
		});
		
		if(this.value instanceof Integer) {
			Integer ret = (Integer) this.value;
			this.value = tmp;
			return ret;
		}
		
		Double returnValue = (Double) this.value;
		this.value = tmp;
		return returnValue.intValue();
	}
}
