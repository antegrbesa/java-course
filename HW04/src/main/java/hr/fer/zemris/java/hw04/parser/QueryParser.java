package hr.fer.zemris.java.hw04.parser;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw04.db.ComparisonOperators;
import hr.fer.zemris.java.hw04.db.ConditionalExpression;
import hr.fer.zemris.java.hw04.db.FieldValueGetters;
import hr.fer.zemris.java.hw04.db.IComparisonOperator;
import hr.fer.zemris.java.hw04.db.IFieldValueGetter;
import hr.fer.zemris.java.hw04.db.StudentRecord;
import hr.fer.zemris.java.hw04.db.Token;
import hr.fer.zemris.java.hw04.db.TokenType;
import hr.fer.zemris.java.hw04.lexer.QueryLexer;
import hr.fer.zemris.java.hw04.lexer.QueryLexerException;

/**
 * Implementation of a parser that makes use of a {@link QueryLexer}. This parser is used
 * for a simple database emulator. Rules of a simple query-based database language that this parser
 * parses are as follows: 
 * 
 * <ul>
 * <li> There is only one command <i>query</i> which is used for retrieval of certain records and every command line
 * must start with it
 * <li> After <i>query</i>, everything that follows most be formatted as: <code>attributeName+operator+stringLiteral</code>
 * (blanks between are allowed). For attribute names, see {@link StudentRecord}, for operators {@link ComparisonOperators}.
 * <li> For creating more than one conditional expression, use <i>and</i> keyword. 
 * <li> String literals must be written in quotes, and quote can not be written in string.
 * </ul>
 *  
 * <p>Please note that logical operator <i>AND</i> can be written with any casing, but command names, attribute names and literals
 * are case sensitive. Also, query command performs search in two different ways:
 * <ul>
 * <li>If query is given only a single attribute (which must be jmbag) and a comparison operator is =, the command obtains the requested 
 * using the indexing facility of  database implementation in O(1) complexity.
 * <li>For any other query (a single jmbag but operator is not =, or any query having more than one attribute), the command performs sequential
 *  record filtering using the given expressions.
 *  </ul>
 *  
 * @author Ante Grbesa
 *
 */
public class QueryParser {
	
	/**Lexer that generates tokens from text*/
	private QueryLexer lexer;
	
	/**Collection of processed conditional expressions*/
	private List<ConditionalExpression> list;
	
	/**Flag for checking if query was direct*/
	private boolean directQuery;
	
	/**Flag for query tag at the beginning*/
	private boolean queryTag;
	
	/**
	 * Creates an instance of this class and immediately tries to parse given text.
	 * @param text text to parse
	 * @throws QueryParserException if an error ocurred during parsing
	 */
	public QueryParser(String text) {
		if(text == null) {
			throw new IllegalArgumentException("Input text is null.");
		}
		
		lexer = new QueryLexer(text);
		list = new ArrayList<>();
		directQuery = false;
		queryTag = false;
		
		try {
			parse();
		} catch(QueryLexerException e) {
			throw new QueryParserException(e.getMessage());
		}
	}
	
	/**
	 * Checks if the current query is direct query.
	 * @return true if current query is direct, false otherwise
	 */
	public boolean isDirectQuery() {
		return directQuery;
	}
	
	/**
	 * Returns the string  which was given in equality comparison in direct query.
	 * @return string  which was given in equality comparison in direct query.
	 * @throws IllegalStateException if current query is not direct 
	 */
	public String getQueriedJMBAG() {
		if(!directQuery) {
			throw new IllegalStateException("Query is not direct");
		}
		
		return list.get(0).getStringLiteral();
	}
	
	/**
	 * Returns a list of {@link ConditionalExpression} from query.
	 * @return  list of {@link ConditionalExpression}
	 */
	public List<ConditionalExpression> getQuery() {
		return list;
	}
	
	/**
	 * Tries to parse specified text.
	 * @throws QueryParserException if an error occured
	 */
	private void parse() {
		Token token;
		while((token = lexer.nextToken()).getType() != TokenType.EOF) {
			TokenType type = token.getType();
			
			if(!queryTag) {
				if(token.getValue().equals("query")) {
					queryTag = true;
					continue;
				} else {
					throw new QueryParserException("Command must start with 'query' keyword");
				}
			}
			
			if(type == TokenType.IDENTIFIER) {
				boolean and = false;
				
				if(token.getValue().toUpperCase().equals("AND")) {
					if(list.size() == 0) {
						throw new QueryParserException("Invalid use of AND operator");
					} else {
						and = true;
						token = lexer.nextToken();
					}
				}
				
				if(!and && list.size() != 0) {
					throw new QueryParserException("No AND between expressions");
				}
				
				IFieldValueGetter getter = getFieldGetter(token);
				token = lexer.nextToken();
				if(token.getType() != TokenType.OPERATOR) {
					throw new QueryParserException("Expected operator, was "+token.getType());
				}
				IComparisonOperator operator = getOperator(token);
				token = lexer.nextToken();
				if(token.getType() != TokenType.STRING) {
					throw new QueryParserException("Expected string, was "+token.getType());
				}
				String literal = token.getValue();
				ConditionalExpression expr = new ConditionalExpression(getter, literal, operator);
				checkIfDirect(expr);
				list.add(expr);
			} else {
				throw new QueryParserException("Invalid attribute name");
			}
			
		}
		
		
	}
	
	/**
	 * Checks if given conditional expression is a direct one.
	 * @param expr expression to evaluate
	 */
	private void checkIfDirect(ConditionalExpression expr) {
		if(!directQuery) {
			if(expr.getFieldGetter() == FieldValueGetters.JMBAG
					&& expr.getComparisonOperator() == ComparisonOperators.EQUALS) {
				directQuery = true;
			}
			return;
		}
		
		directQuery = false;
	}
	
	/**
	 * Returns a member of ComparisonOperators generated from given token.
	 * @param token token to get the operator from
	 * @return newly created ComparisonOperator
	 * @throws QueryParserException if given token is invalid operator
	 */
	private IComparisonOperator getOperator(Token token) {
		String oper = token.getValue().toUpperCase();
		
		switch(oper) {
		case("<") : 
			return ComparisonOperators.LESS;
		case("<=") :
			return ComparisonOperators.LESS_OR_EQUALS;
		case(">") :
			return ComparisonOperators.GREATER;
		case(">=") :
			return ComparisonOperators.GREATER_OR_EQUALS;
		case("!=") :
			return ComparisonOperators.NOT_EQUALS;
		case("=") :
			return ComparisonOperators.EQUALS;
		case("LIKE") :
			return ComparisonOperators.LIKE;
		default : 
			throw new QueryParserException("Invalid operator, was "+oper);
		}
	}
	
	/**
	 * Returns a member of FieldValueGetters generated from given token.
	 * @param token token to get the field getterr from
	 * @return newly created FieldValueGetters member
	 * @throws QueryParserException if given token is not a field getter
	 */
	private IFieldValueGetter getFieldGetter(Token token) {
		String value = token.getValue();
		if(value.equals("firstName")) {
			return FieldValueGetters.FIRST_NAME;
		}
		if(value.equals("lastName")) {
			return FieldValueGetters.LAST_NAME;
		}
		if(value.equals("jmbag")) {
			return FieldValueGetters.JMBAG;
		}
		
		throw new QueryParserException("Invalid attribute name, was "+token.getValue());
	}
}
