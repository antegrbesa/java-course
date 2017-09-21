package demo;

import hr.fer.zemris.bf.lexer.Lexer;
import hr.fer.zemris.bf.lexer.LexerException;
import hr.fer.zemris.bf.lexer.Token;
import hr.fer.zemris.bf.lexer.TokenType;

public class Izrazi1 {

	public static void main(String[] args) {
		String[] expressions = new String[] {
			"0",
			"tRue",
			"Not a",
			"A aNd b",
			"a or b",
			"a xoR b",
			"A and b * c",
			"a or b or c",
			"a xor b :+: c",
			"not not a",
			"a or b xor c and d",
			"a or b xor c or d",
			"a xor b or c xor d",
			"(a + b) xor (c or d)",
			"(d or b) xor not (a or c)",
			"(c or d) mor not (a or b)",
			"not a not b",
			"a and (b or",
			"a and (b or c",
			"a and 10"
		};
		
		for(String expr : expressions) {
			System.out.println("==================================");
			System.out.println("Izraz: " + expr);
			System.out.println("==================================");
			
			try {
				System.out.println("Tokenizacija:");
				Lexer lexer = new Lexer(expr);
				Token token = null;
				do {
					token = lexer.nextToken();
					System.out.println("Type "+token.getTokenType()+", Value: "
							+token.getTokenValue()+", instance of: "+token.getTokenValue().getClass());
				} while(token.getTokenType()!=TokenType.EOF);
			} catch(LexerException ex) {
				System.out.println("Iznimka: " + ex.getClass()+" - " + ex.getMessage());
			} catch(NullPointerException ignorable) {
				
			}
			System.out.println();
		}
	}
}
