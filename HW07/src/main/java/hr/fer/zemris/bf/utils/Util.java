package hr.fer.zemris.bf.utils;


import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

import hr.fer.zemris.bf.model.Node;

/**
 * A utility class that offers a couple of useful methods for 
 * boolean expression evaluation. 
 * @author Ante Grbesa
 *
 */
public class Util {
	
	/**
	 * Generates all combinations of values for given variables and calls specified
	 * consumer for every combination that is generated. 
	 * @param variables variables 
	 * @param consumer consumer to call
	 */
	public static void forEach(List<String> variables, Consumer<boolean[]> consumer) {
		int n = variables.size();
		int max = (int) Math.pow(2, n);
		
		for (int i = 0; i < max; i++) {
			String binary = Integer.toBinaryString(i);
			while (binary.length() < n) {
				binary = "0" + binary;	//fill with leading zeroes
			}
			
			boolean[] array = new boolean[n];
			for (int j = 0; j < n; j++) {
				array[j] = binary.toCharArray()[j] == '0' ? false : true;
			}
			consumer.accept(array);
		}
		
	}
	
	/**
	 * Method returns all combinations generated from given variables and expression that 
	 * have the same expression value as the one specified. 
	 * @param variables variables to generate combinations from
	 * @param expression expression to apply on variables
	 * @param expressionValue expression value to compare 
	 * @return all combinations that have the same value as the one specified
	 */
	public static Set<boolean[]> filterAssignments(List<String> variables
			, Node expression, boolean expressionValue) {
		Set<boolean[]> set = new LinkedHashSet<>();
		ExpressionEvaluator evaluator = new ExpressionEvaluator(variables);
		
		forEach(variables, (arr) -> { 
			evaluator.setValues(arr);
			expression.accept(evaluator);
			if (evaluator.getResult() == expressionValue)
				set.add(arr);
		});
		
		return set;
	}
	
	/**
	 * Returns ordinal number of a given array of boolean values. 
	 * @param values values to retrieve ordinal number from
	 * @return  ordinal number of a given array 
	 */
	public static int booleanArrayToInt(boolean[] values) {
		StringBuilder sb = new StringBuilder();
		for (boolean value : values) {
			sb.append(value ? "1" : "0");
		}
		
		int number = Integer.parseUnsignedInt(sb.toString(), 2);
		return number;
	}
	
	/**
	 * Returns a set containing all minterms that specified expression contains. 
	 * @param variables all variables in expression
	 * @param expression expression to extract values from 
	 * @return set containing all minterms
	 */
	public static Set<Integer> toSumOfMinterms(List<String> variables, Node expression) {
		return getTerms(variables, expression, true);
	}
	
	/**
	 * Returns a set containing all maxterms that specified expression contains. 
	 * @param variables all variables in expression
	 * @param expression expression to extract values from 
	 * @return set containing all maxterms
	 */
	public static Set<Integer> toSumOfMaxterms(List<String> variables, Node expression) {
		return getTerms(variables, expression, false);
	}
	
	/**
	 * Generates a minterm or a maxterm (depends on value given) from given expression. 
	 * @param variables variables in expression
	 * @param expression expression
	 * @param value true for minterm, false for maxterm 
	 * @return set containing all minterms or maxterms
	 */
	private static Set<Integer> getTerms(List<String> variables, Node expression, boolean value) {
		Set<boolean[]> values = filterAssignments(variables, expression, value);
		Set<Integer> terms = new TreeSet<>();
		
		for (boolean[] b : values) {
			terms.add(booleanArrayToInt(b));
		}
		
		return terms;
	}
	
}
