package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import hr.fer.zemris.java.hw04.parser.QueryParser;
import hr.fer.zemris.java.hw04.parser.QueryParserException;

public class QueryParserTest {

	@Test
	public void testAverageInput() {
		String text = "query lastName = \"Blažić\"";
		QueryParser parser = new QueryParser(text);
		
		List<ConditionalExpression> list = parser.getQuery();
		ConditionalExpression ex =  new ConditionalExpression
				(FieldValueGetters.LAST_NAME, "Blažić", ComparisonOperators.EQUALS);
		
		assertEquals(list.get(0).getFieldGetter(), ex.getFieldGetter());
		assertEquals(list.get(0).getComparisonOperator(), ex.getComparisonOperator());
		assertEquals(list.get(0).getStringLiteral(), ex.getStringLiteral());
	}
	
	@Test
	public void testComplicatedInput() {
		String text = "query firstName>\"A\" and firstName<\"C\" and lastName LIKE \"B*ć\" and jmbag>\"0000000002\"";
		QueryParser parser = new QueryParser(text);
		
		List<ConditionalExpression> list = parser.getQuery();
		
		assertFalse(parser.isDirectQuery());
		assertEquals(list.get(0).getFieldGetter(), FieldValueGetters.FIRST_NAME);
		assertEquals(list.get(0).getComparisonOperator(), ComparisonOperators.GREATER);
		assertEquals(list.get(0).getStringLiteral(), "A");
		assertEquals(list.get(1).getFieldGetter(), FieldValueGetters.FIRST_NAME);
		assertEquals(list.get(1).getComparisonOperator(), ComparisonOperators.LESS);
		assertEquals(list.get(1).getStringLiteral(), "C");
		assertEquals(list.get(2).getFieldGetter(), FieldValueGetters.LAST_NAME);
		assertEquals(list.get(2).getComparisonOperator(), ComparisonOperators.LIKE);
		assertEquals(list.get(2).getStringLiteral(), "B*ć");
		assertEquals(list.get(3).getFieldGetter(), FieldValueGetters.JMBAG);
		assertEquals(list.get(3).getComparisonOperator(), ComparisonOperators.GREATER);
		assertEquals(list.get(3).getStringLiteral(), "0000000002");
	}
	
	@Test
	public void testDirectQuery() {
		String text = "query jmbag  =  \"0000000003\"";
		QueryParser parser = new QueryParser(text);
		
		assertTrue(parser.isDirectQuery());
		assertEquals("0000000003", parser.getQueriedJMBAG());
	}
	
	@Test(expected = QueryParserException.class)
	public void testWithoutAnd() {
		String text = "query jmbag<\"0000000003\" firstName<\"C\"";
		QueryParser parser = new QueryParser(text);
	}
	
	@Test(expected = QueryParserException.class)
	public void testFirstAnd() {
		String text = "query and jmbag<\"0000000003\" and firstName<\"C\"";
		QueryParser parser = new QueryParser(text);
	}
	
	@Test(expected = QueryParserException.class)
	public void testInvalidOperator() {
		String text = "query jmbag!\"0000000003\" firstName<\"C\"";
		QueryParser parser = new QueryParser(text);
	}
	
	@Test(expected = QueryParserException.class)
	public void testInvalidOperators() {
		String text = "query jmbag   < > \"0000000003\" firstName<\"C\"";
		QueryParser parser = new QueryParser(text);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testDirectQueryExeption() {
		String text = "query jmbag<\"0000000003\"";
		QueryParser parser = new QueryParser(text);
		
		assertFalse(parser.isDirectQuery());
		parser.getQueriedJMBAG();
	}
	
	@Test(expected = QueryParserException.class)
	public void testInvalidLike() {
		String text = "query jmbag<\"0000000003\" and like \"C\" ";
		QueryParser parser = new QueryParser(text);
		
		assertFalse(parser.isDirectQuery());
		parser.getQueriedJMBAG();
	}
	
	@Test
	public void testEmptyInput() {
		String text = " ";
		QueryParser parser = new QueryParser(text);
		
		assertTrue(parser.getQuery().size() == 0);
	}

}
