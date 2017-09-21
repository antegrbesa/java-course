package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class servers as a middle-man for communication with web client and is used 
 * by {@link SmartHttpServer} class. 
 * @author Ante Grbeša
 *
 */
public class RequestContext {

	/**Output Stream*/
	private OutputStream outputStream;
	
	/**Character set used*/
	private Charset charset;
	
	/**Encoding used*/
	private String encoding = "UTF-8";
	
	/**Status code to write, default 200 */
	private int statusCode = 200;
	
	/**Status message to write, default OK */
	private String statusText = "OK";
	
	/**Mime type used */
	private String mimeType = "text/html";
	
	/**Given parameters*/
	private Map<String, String> parameters;
	
	/**Given temporary parameters*/
	private Map<String, String> temporaryParameters;
	
	/**Given persistent parameters*/
	private Map<String, String> persistentParameters;
	 
	/**Collection of cookies*/
	private List<RCCookie> outputCookies;
	
	/**Flag for header generation*/
	private boolean headerGenerated = false;
	
	/**Length of content */
	private Long contentLength;
	
	/**Dispatcher used*/
	private IDispatcher dispatcher;
	
	/**
	 * Constructs an instance of this class using given arguments. 
	 * @param outputStream output stream to write to
	 * @param parameters parameters to set
	 * @param persistentParameters persistent parameters to set 
	 * @param outputCookies cookies to set
	 * @param contentLength length of content
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies, Long contentLength) {
		this(outputStream, parameters, persistentParameters, outputCookies, contentLength, null, null);
	}

	/**
	 * Constructs an instance of this class using given arguments. 
	 * @param outputStream output stream to write to
	 * @param parameters parameters to set
	 * @param persistentParameters persistent parameters to set 
	 * @param outputCookies cookies to set
	 * @param contentLength length of content
	 * @param temporaryParameters temporary parameters to set
	 * @param dispatcher dispatcher to set
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies,
			Long contentLength, Map<String, String> temporaryParameters, IDispatcher dispatcher) {
		super();
		if (outputStream == null) {
			throw new IllegalArgumentException("Output stream was null");
		}
		this.outputStream = outputStream;
		this.parameters = parameters == null ? new HashMap<>() : parameters;
		this.persistentParameters = persistentParameters == null ? new HashMap<>() : persistentParameters;
		this.outputCookies = outputCookies  == null ? new ArrayList<>() : outputCookies;
		this.temporaryParameters = temporaryParameters == null ? new HashMap<>() : temporaryParameters;
		this.dispatcher = dispatcher;
		this.contentLength = contentLength;
	}

	/**
	 * Gets parameter for given key. 
	 * @param name key for param
	 * @return value under given key
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Gets parameter keys 
	 * @return all parameter names
	 */
	public Set<String> getParameterNames() {
		return getNames(parameters);
	}

	/**
	 * Gets persistent parameter for given key. 
	 * @param name key for param
	 * @return value under given key
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}
	
	/**
	 * Gets persistent parameter keys 
	 * @return all parameter names
	 */
	public Set<String> getPersistentParameterNames() {
		return getNames(persistentParameters);
	}
	
	/**
	 * Sets a persistent parameter using given name and value.
	 * @param name name to use
	 * @param value value to put 
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}

	/**
	 * Removes persistent parameter under given name.
	 * @param name key of parameter
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	/**
	 * Gets temporary parameter keys
	 * @param name key for value
	 * @return  parameter name
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}
	
	/**
	 * Sets content length. 
	 * @param contentLength length to set
	 */
	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}
	
	/**
	 * Returns dispatcher currently used. 
	 * @return dispatcher
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}
	
	/**
	 * Gets temporary parameter keys 
	 * @return all parameter names
	 */
	public Set<String> getTemporaryParameterNames() {
		return getNames(temporaryParameters);
	}
	
	/**
	 * Returns all keys from a map in a unmodifiable set. 
	 * @param map map containing keys
	 * @return all keys from a map
	 */
	private Set<String> getNames(Map<String, String> map) {
		Set<String> set = new HashSet<>();
		
		map.forEach((k,v) -> set.add(k));
		
		return Collections.unmodifiableSet(set);
	}
	
	/**
	 * Sets a temporary parameter.
	 * @param name name to set
	 * @param value value to set
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}
	
	/**
	 * Removes temporary parameter
	 * @param name name for parameter
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}
	
	/**
	 * Checks if a header was generated, if it has, throws exception.
	 */
	private void checkHeaderGeneration() {
		if (headerGenerated) {
			throw new RuntimeException("Header already generated using previous value.");
		}
	}
	
	/**
	 * Sets encoding only if a header hasn't been generated. 
	 * @param encoding  encoding to set
	 */
	public void setEncoding(String encoding) {
		checkHeaderGeneration();
		this.encoding = encoding;
	}
	
	/**
	 * Sets status code only if a header hasn't been generated. 
	 * @param statusCode status code to set
	 */
	public void setStatusCode(int statusCode) {
		checkHeaderGeneration();
		this.statusCode = statusCode;
	}
	
	/**
	 * Sets status text only if a header hasn't been generated. 
	 * @param statusText text to set 
	 */
	public void setStatusText(String statusText) {
		checkHeaderGeneration();
		this.statusText = statusText;
	}
	
	/**
	 * Sets mime type only if a header hasn't been generated. 
	 * @param mimeType mime type to set
	 */
	public void setMimeType(String mimeType) {
		checkHeaderGeneration();
		this.mimeType = mimeType;
	}
	
	/**
	 * Sets output cookies collection only if a header hasn't been generated. 
	 * @param outputCookies cookies to set
	 */
	public void setOutputCookies(List<RCCookie> outputCookies) {
		checkHeaderGeneration();
		this.outputCookies = outputCookies;
	}
	
	/**
	 * Writes to specified output stream.  
	 * @param data data to write
	 * @return  this RequestContext
	 * @throws IOException if an IO exception occurred
	 */
	public RequestContext write(byte[] data) throws IOException {
		if (! headerGenerated) {
			generateHeader();
		}
		
		outputStream.write(data);	
		return this;
	}
	
	/**
	 * Writes to specified output stream.  
	 * @param text data to write
	 * @return  this RequestContext
	 * @throws IOException if an IO exception occurred
	 */
	public RequestContext write(String text) throws IOException {
		if (! headerGenerated) {
			generateHeader();
		}
		
		byte[] data = text.getBytes(charset);
		write(data);
		return this;
	}
	
	/**
	 * Generates header and writes it to output stream.
	 * @throws IOException if an exception occurred
	 */
	private void generateHeader() throws IOException {
		charset = Charset.forName(encoding);
		
		StringBuilder sb = new StringBuilder();
		
		//first line
		sb.append("HTTP/1.1 ");
		sb.append(statusCode);
		sb.append(" ");
		sb.append(statusText);
		sb.append("\r\n");
		
		//second line
		sb.append("Content-type: ");
		sb.append(mimeType);
		if (mimeType.startsWith("text/")) {
			sb.append(" ;charset= ");
			sb.append(encoding);
		}
		sb.append("\r\n");
		
		//third line
		if (contentLength > 0) {
			sb.append("Content-Length: ");
			sb.append(contentLength);
			sb.append("\r\n");
		}
		
		//fourth line
		getOutputCookies(sb);
		sb.append("\r\n");
		
		outputStream.write(sb.toString().getBytes(Charset.forName("ISO_8859_1")));
		
		headerGenerated = true;
	}

	/**
	 * Builds a {@code Set-Cookie} line for header using specified builder. 
	 * @param sb builder to use
	 */
	private void getOutputCookies(StringBuilder sb) {
		outputCookies.forEach((c) -> { 
			sb.append("Set-Cookie: ");
			sb.append(c.name);
			sb.append("=");
			sb.append("\"");
			sb.append(c.value);
			sb.append("\"; ");

			if (c.domain != null) {
				sb.append("Domain=");
				sb.append(c.domain);
				sb.append("; ");
			}
			if (c.path != null) {
				sb.append("Path=");
				sb.append(c.path);
				sb.append("; ");
			}
			if (c.maxAge != null) {
				sb.append("Max-Age=");
				sb.append(c.maxAge);
				sb.append("; ");
			}
			sb.replace(sb.length()-2, sb.length(), "");	//remove last ;
			sb.append("\r\n");
		});
	}

	/**
	 * Adds a cookie to internal collection of cookies. 
	 * @param cookie cookie to add
	 */
	public void addRCCookie(RCCookie cookie) {
		outputCookies.add(cookie);
	}

	/**
	 * Represents a single cookie used by {@link SmartHttpServer} class.
	 * @author Ante Grbeša
	 *
	 */
	public static class RCCookie {
		
		/**Name of a cookie*/
		private String name;
		
		/**Value of a cookie*/
		private String value;
		
		/**Domain of a cookie*/
		private String domain;
		
		/**Path of a cookie*/
		private String path;
		
		/**Maximum age of a cookie*/
		private Integer maxAge;

		/**
		 * Constructs an instance of this class using specified arguments. 
		 * @param name name to set
		 * @param value value to set
		 * @param maxAge maximum age to set
		 * @param domain domain to set
		 * @param path path to set
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * Gets the name. 
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Gets the value. 
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Gets the domain. 
		 * @return the domain
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * Gets the path. 
		 * @return the path
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Gets the maximum age. 
		 * @return the maxAge
		 */
		public Integer getMaxAge() {
			return maxAge;
		}
	}
}
