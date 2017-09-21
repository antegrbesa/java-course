package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * A simple test class for {@link SmartScriptEngine}.
 * @author Ante
 *
 */
public class SmartScriptEngineTest {

	/**
	 * Main method
	 * @param args not used
	 */
	public static void main(String[] args) {
		Path path = Paths.get("src//main//resources//zbrajanje.smscr");
		List<String> docBody;
		try {
			docBody = Files.readAllLines(path);
		} catch (IOException e) {
			System.out.println("Error with file");
			return;
		}
		StringBuilder sb = new StringBuilder();
		docBody.forEach((s) ->  { 
			sb.append(s);
			sb.append("\n");
		});
		Map<String, String> parameters = new HashMap<>();
		Map<String, String> persistentParameters = new HashMap<>();
		List<RCCookie> cookies = new ArrayList<>();
		parameters.put("a", "4");
		parameters.put("b", "2");
		RequestContext rc = new RequestContext(System.out, parameters, persistentParameters, cookies, 0L);
		
		new SmartScriptEngine(new SmartScriptParser(sb.toString()).getDocumentNode(),
				rc).execute();
		
	}

}
