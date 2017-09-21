package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;
import hr.fer.zemris.java.hw04.db.ConditionalExpression;

import org.junit.Before;
import org.junit.Test;

public class ConditionalExpressionTest {

	@Test
	public void test() {
		ConditionalExpression expr = new ConditionalExpression(
				  FieldValueGetters.LAST_NAME,
				  "Bos*",
				  ComparisonOperators.LIKE
		);
		
		StudentRecord record = new StudentRecord("0036412331", "Ivan", "Bosić", 5);
		
		boolean recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),  // returns lastName from given record
				expr.getStringLiteral()             // returns "Bos*"
		);
		
		assertTrue(recordSatisfies);
	}

}
