package hr.fer.zemris.java.webserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Represents a simple web server. For configuration, it uses files located in /config/. 
 * By default, it listens on address 127.0.0.1 and on port 5721. 
 * 
 * All scripts are located in /scripts/.
 * All files are located in webroot folder. 
 * 
 * To start this server, write {@code start} in console. To stop it, write {@code stop} in console.
 * Expects a single command line argument, path to server.properties file. If no command line argument is given, 
 * it uses default properties file located in /config(.
 * This server also has a background collector that collects and removes old sessions every 5 minutes. 
 * @author Ante Grbeša 
 *
 */
public class SmartHttpServer {
	
	/**
	 * Address of server. 
	 */
	private String address;
	
	/**Server port*/
	private int port;
	
	/**Number of worker threads*/
	private int workerThreads;
	
	/**Session time out*/
	private int sessionTimeout;
	
	/**Collection of supported mime types.*/
	private Map<String, String> mimeTypes = new HashMap<>();
	
	/**Server thread*/
	private ServerThread serverThread;
	
	/**Thread pool*/
	private ExecutorService threadPool;
	
	/**Document root location*/
	private Path documentRoot;
	
	/**Workers map*/
	private Map<String, IWebWorker> workersMap = new HashMap<>(); 
	
	/**Collection of sessions*/
	private volatile Map<String, SessionMapEntry> sessions = new ConcurrentHashMap<>();
	
	/**Random generator*/
	private Random sessionRandom = new Random();
	
	/**Characters for random string generation*/
	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	/**
	 * Main method. 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		String configFileName = "./config/server.properties";
		if(args.length!=1) {
			System.out.println("No valid arguments given, continuing with default configuration.");
		} else {
			configFileName = args[0];
		}
		
		SmartHttpServer server = new SmartHttpServer(configFileName);
		
		System.out.println("Type start to start server; type stop to stop server and terminate application");
		
		Scanner sc = new Scanner(System.in);
		while (!sc.nextLine().equals("start"));
		server.start();
		System.out.println("Server started.");
		
		while (!sc.nextLine().equals("stop"));
		server.stop();
		System.out.println("Server stopped.");
		
		sc.close();
	}
	
	/**
	 * Constructs an instance of this class and reads all properties from specified
	 * path.
	 * @param configFileName path to configuration file
	 */
	public SmartHttpServer(String configFileName) {
		Properties properties = new Properties();
		try {
			properties.load(Files.newInputStream(Paths.get(configFileName)));
		} catch (IOException e) {
			throw new IllegalArgumentException("Invalid file, was "+configFileName);
		}
		address = properties.getProperty("server.address");
		port = Integer.parseInt(properties.getProperty("server.port"));
		workerThreads = Integer.parseInt(properties.getProperty("server.workerThreads"));
		sessionTimeout = Integer.parseInt(properties.getProperty("session.timeout"));
		documentRoot = Paths.get( properties.getProperty("server.documentRoot"));
		
		String workersConfig = properties.getProperty("server.workers");
		String mimeConfig = properties.getProperty("server.mimeConfig");
		
		extractMimes(properties, mimeConfig);	
		extractWorker(properties, workersConfig);
		
		serverThread = new ServerThread();	
	}
	
	/**
	 * Extracts mime types from given path.
	 * @param properties used for parsing
	 * @param mimeConfig path to file
	 */
	private void extractMimes(Properties properties, String mimeConfig) {
		try {
			properties.load(Files.newInputStream(Paths.get(mimeConfig)));
		} catch (IOException e) {
			throw new IllegalArgumentException("Invalid file, was "+mimeConfig);
		}
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			String arg1 = (String) entry.getKey();
			String arg2 = (String) entry.getValue();
			mimeTypes.put(arg1, arg2);
		}
	}
	
	/**
	 * Extracts worker properties from given path.
	 * @param properties used for parsing
	 * @param workersConfig path to file
	 */
	private void extractWorker(Properties properties, String workersConfig) {
		properties = new Properties();
		try {
			properties.load(Files.newInputStream(Paths.get(workersConfig)));
		} catch (IOException e) {
			throw new IllegalArgumentException("Invalid file, was "+workersConfig);
		}
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			String path = (String) entry.getKey();
			String fqcn = (String) entry.getValue();
			
			Class<?> referenceToClass = null;
			try {
				referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
			} catch (ClassNotFoundException e) {
				System.out.println("Error 1: "+e.getMessage());
				return;
			}
			Object newObject = null;
			try {
				newObject = referenceToClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				System.out.println("Error 2 : "+e.getMessage());
				return;
			}
			IWebWorker iww = (IWebWorker) newObject;
			workersMap.put(path, iww);
		}
	}
	
	/**
	 * Starts this server if it hasn't already been started.
	 */
	protected synchronized void start() {
		if (! serverThread.isAlive()) {
			threadPool = Executors.newFixedThreadPool(workerThreads);
			serverThread.start();
			Thread collector = new Thread(() -> { 
				try {
					Thread.sleep(300000);
				} catch (InterruptedException e) {
					System.out.println("Error in collector: "+e.getMessage());
				}
				Map<String, SessionMapEntry> map = new HashMap<>(sessions);
				map.entrySet().forEach((e) -> { 
					if (e.getValue().validUntil < Instant.now().getEpochSecond()) {
						sessions.remove(e.getKey());
					}
				});
			});
			collector.setDaemon(true);
			collector.start();
		}
	}
	
	/**
	 * Stops this server. 
	 */
	protected synchronized void stop() {
		serverThread.terminate();
		try {
			serverThread.join();
		} catch (InterruptedException e1) {
			System.out.println("Error: "+e1.getMessage());
		}
		
		threadPool.shutdown();
		try {
			// Wait 30 seconds for existing tasks to terminate
			if (!threadPool.awaitTermination(30, TimeUnit.SECONDS)) {
				threadPool.shutdownNow(); 
				// Wait 30 seconds for tasks to respond to being cancelled
				if (!threadPool.awaitTermination(30, TimeUnit.SECONDS)) {
					System.err.println("Pool did not terminate");
				}
			}
		} catch (InterruptedException e) {
			threadPool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
		
	}
	
	/**
	 * Represents main server thread that controls this server. 
	 * @author Ante Grbeša
	 *
	 */
	protected class ServerThread extends Thread { 
		
		/**Flag for termination*/
		private volatile boolean running = true;
		
		/**
		 * Terminates this thread.
		 */
		public void terminate() {
			running = false;
		}
		
		@Override
		public void run() {
			try (ServerSocket serverSocket = new ServerSocket())  {
				
				serverSocket.bind(new InetSocketAddress((InetAddress) null, port));
				serverSocket.setSoTimeout(5000); //5-second timeout
				
				while (running) {
					Socket client=null;
					try {
						client = serverSocket.accept();
					} catch (SocketTimeoutException se) {
						if (running) continue;
						else break;
					}
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}
			} catch (Exception e) {
				System.out.println("Error: "+e.getMessage());
			}
		}
	}
	
	/**
	 * Generates a random SID string. 
	 * @return generated string
	 */
	private  String generateSID() {
		int len = 20; //length of string
		StringBuilder sb = new StringBuilder(20);
		for( int i = 0; i < len; i++ ) {
		      sb.append( AB.charAt(sessionRandom.nextInt(AB.length())));
		}
		return sb.toString();
	}
	
	/**
	 * Represents a single session entry. 
	 * @author Ante Grbeša
	 *
	 */
	private static class SessionMapEntry {
		/**SID for this session*/
		String sid;
		
		/**Valid until number*/
		long validUntil;
		
		/**Stores client's data*/
		Map<String, String> map;

		/**
		 * Creates an instance of this class using specified parameters. 
		 * @param sid sid to set
		 * @param validUntil valid until number
		 * @param map map to set
		 */
		public SessionMapEntry(String sid, long validUntil, Map<String, String> map) {
			super();
			this.sid = sid;
			this.validUntil = validUntil;
			this.map = map;
		}
		
		
	}
	
	/**
	 * Represents a single client. Also implements {@link IDispatcher}. 
	 * @author Ante Grbeša
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {
		
		/**Client scoket*/
		private Socket csocket;
		
		/**Input stream*/
		private PushbackInputStream istream;
		
		/**Output stream*/
		private OutputStream ostream;
		
		/**HTTP version*/
		private String version;
		
		/**Method*/
		private String method;
		
		/**Parameters collection*/
		private Map<String, String> params = new HashMap<>();
		
		/**Temporary parameters collection*/
		private Map<String, String> tempParams = new HashMap<>();
		
		/**Permanent parameters collection*/
		private Map<String, String> permParams = new HashMap<>();
		
		/**Cookies collection*/
		private List<RCCookie> 	outputCookies = new ArrayList<>();
		
		/**Session ID*/
		private String SID;
		
		/**Request context */
		private RequestContext context = null;
		
		/**
		 * Creates an instance of this class with specified client socket.
		 * @param csocket client socket
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}
		
		@Override
		public void run() {
			try {
			istream = new PushbackInputStream(csocket.getInputStream());
			ostream = csocket.getOutputStream();
			byte[] request = readRequest(istream);
			if (request == null) {
				sendError(ostream, 400, "Bad request");
				return;
			}
			
			List<String> headers = getHeaders(new String(request, StandardCharsets.US_ASCII));
			if (headers.size() < 1) {
				sendError(ostream, 400, "Bad request");
			}
			String[] firstLine = headers.get(0).split(" ");
			
			method = firstLine[0].toUpperCase();
			if(!method.equals("GET")) {
				sendError(ostream, 405, "Method Not Allowed");
				return;
			}
			
			version = firstLine[2].toUpperCase();
			if(!version.equals("HTTP/1.1") && !version.equals("HTTP/1.0")) {
				sendError(ostream, 505, "HTTP Version Not Supported");
				return;
			}
			
			checkSession(headers);
			
			String reqPath = firstLine[1];
			String path;
			String paramString = "";
			if (reqPath.contains("?")) {
				String[] splitPath = reqPath.split("[?]");
				paramString = splitPath[1];
				path = splitPath[0];
				parseParameters(paramString);
				
			} else {
				path = reqPath;
			}
			internalDispatchRequest(path, true); 
			
			csocket.close();
			ostream.close();
			istream.close();
			
			} catch (Exception e) {
				System.out.println("Error in client worker: "+e.getMessage());
			} 
			
			
		}

		/**
		 * Checks cookies for current session and sets them accordingly.
		 * @param headers headers list
		 */
		private synchronized void checkSession(List<String> headers) {
			String host = address;
			String sidCandidate = null;
			for (String l : headers) {
				if (l.startsWith("Host:")) {
					String[] contents = l.split("[:]");
					if (contents.length == 3) {
						host = contents[1].trim();
					}
					continue;
				}
				if (! l.startsWith("Cookie:")) {
					continue;
				}
				
				String[] parsed = l.split(":");

				if (parsed.length == 2) {
					String[] cookies = parsed[1].split("[;]");
					for (String c : cookies) {
						if (c.trim().startsWith("sid")) {
							sidCandidate = c.split("[=]")[1];
							break;
						}
					}
				}
			}
			
			SessionMapEntry entry = null;
			if (sidCandidate == null) {
				entry = createCookie(host);	
			} else {		
				entry = sessions.get(sidCandidate.replaceAll("\"", ""));
				if (entry == null) {
					entry = createCookie(host);
				} else if (entry.validUntil < Instant.now().getEpochSecond()) {
					sessions.remove(sidCandidate);
					entry = createCookie(host);
				} else {
					entry.validUntil =  Instant.now().getEpochSecond() + sessionTimeout;
				}
			}
			
			permParams = entry.map;
		}
			
		/**
		 * Creates a {@link SessionMapEntry} for this session. 
		 * @param host host to set
		 * @return newly created session map entry
		 */
		private SessionMapEntry createCookie(String host) {
			SID = generateSID();
			SessionMapEntry entry =  new SessionMapEntry
					(SID, Instant.now().getEpochSecond()+sessionTimeout, new ConcurrentHashMap<>());
			sessions.put(SID, entry);
			outputCookies.add(new RCCookie("sid", SID, null, host, "/"));
			return entry;
		}

		/**
		 * Gets extension from specified string. 
		 * @param path string containing extension
		 * @return extension from specified string
		 */
		private String getExtension(String path) {
			String extension = "";
			int i = path.lastIndexOf('.');
			if (i > 0) {
			    extension = path.substring(i+1);
			}
			return extension;
		}
		
		/**
		 * Analyzes given path and acts accordingly. 
		 * @param urlPath path to analyze
		 * @param directCall flag for internal call
		 * @throws Exception throws if an exception occurred
		 */
		public void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			Path requestedPath = Paths.get(documentRoot.toString()+urlPath);
			if (!requestedPath.startsWith(documentRoot)) {
				sendError(ostream, 403, "Forbidden");
				return;
	        }
			
			if (urlPath.startsWith("/private/") || urlPath.equals("/private")) {
				if (directCall) {
					sendError(ostream, 404, "File not found");
				}
			}
			
			// convention-over-configuration approach
			if (urlPath.startsWith("/ext/")) {
				String[] names = urlPath.split("[/]");	//split by /
				String name = names[names.length-1].split("[?]")[0];	//get name of class
				Class<?> referenceToClass = null;
				try {
					referenceToClass = this.getClass().getClassLoader().loadClass
							("hr.fer.zemris.java.webserver.workers."+name);
				} catch (ClassNotFoundException e) {
					sendError(ostream, 404, "File not found");
					return;
				}
				Object newObject = null;
				try {
					newObject = referenceToClass.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					System.out.println("Error 2 : "+e.getMessage());
					return;
				}
				IWebWorker iww = (IWebWorker) newObject;
				
				context = context == null ? new RequestContext
						(ostream, params, permParams, outputCookies, 0L, tempParams, this) : context;
				iww.processRequest(context);
				return;
			}
			
			// configuration-based approach
			if (workersMap.containsKey(urlPath)) {
				context = context == null ? new RequestContext
						(ostream, params, permParams, outputCookies, 0L, tempParams, this) : context;
				workersMap.get(urlPath).processRequest(context);
				return;
			}
			
			//check if file exists
			File file = requestedPath.toFile();
			if (! (file.exists() && file.isFile() && file.canRead())) {
				sendError(ostream, 404, "File not found");
				return;
			}
			
			//get mime type from extension
			String extension = getExtension(urlPath);
			String mimeType = mimeTypes.get(extension);
			if (mimeType == null) {
				mimeType = "text/plain";
			}
			
			// if it's a script, run script	
			if (extension.equals("smscr")) {
				String text = getText(requestedPath);
				SmartScriptParser script = new SmartScriptParser(text);
				if (context == null) {
					context =  new RequestContext(ostream, params, permParams, outputCookies, 0L, tempParams, this);
					context.setMimeType(mimeType);
					context.setStatusCode(200);
				}
				SmartScriptEngine engine = new SmartScriptEngine(script.getDocumentNode(), context);
				engine.execute();
				return;
			} 	
			
			//else process normal request
			context = context == null ? new RequestContext
					(ostream, params, permParams, outputCookies, Files.size(requestedPath)) : context;
			context.setMimeType(mimeType);
			context.setStatusCode(200);
			context.write(Files.readAllBytes(requestedPath));
			ostream.flush();			
		}
		
		/**
		 * Gets text from file at specified path. 
		 * @param requestedPath path to file
		 * @return text in file
		 */
		private String getText(Path requestedPath) {
			List<String> docBody;
			try {
				docBody = Files.readAllLines(requestedPath);
			} catch (IOException e) {
				System.out.println("Error with file");
				return "";
			}
			StringBuilder sb = new StringBuilder();
			docBody.forEach((s) ->  { 
				sb.append(s);
				sb.append("\n");
			});
			
			return sb.toString();
		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);			
		}
		
		/**
		 * Parses parameters from string and stores them in internal collection.
		 * @param paramString string containing parameters
		 */
		private void parseParameters(String paramString) {
			String[] parovi = paramString.split("[&]");
			for (String par : parovi) {
				String[] polje = par.split("[=]");
				if (polje.length != 2) continue;
				params.put(polje[0], polje[1]);
			}
			
		}
		
		/**
		 * A simple automata for reading request header.
		 * @param istream2 input stream
		 * @return read data as byte array
		 * @throws IOException if an exception occurred
		 */
		private byte[] readRequest(PushbackInputStream istream2) throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
l:			while(true) {
				int b = istream2.read();
				if(b==-1) return null;
				if(b!=13) {
					bos.write(b);
				}
				switch(state) {
				case 0: 
					if(b==13) { state=1; } else if(b==10) state=4;
					break;
				case 1: 
					if(b==10) { state=2; } else state=0;
					break;
				case 2: 
					if(b==13) { state=3; } else state=0;
					break;
				case 3: 
					if(b==10) { break l; } else state=0;
					break;
				case 4: 
					if(b==10) { break l; } else state=0;
					break;
				}
			}
			
			return bos.toByteArray();	
		}
		
		/**
		 * Gets a header collection from given string.
		 * @param header string containing header
		 * @return header line list
		 */
		private  List<String> getHeaders(String header) {
			List<String> headers = new ArrayList<String>();
			String currentLine = null;
			
			for(String s : header.split("\n")) {
				if(s.isEmpty())
					break;
				
				char c = s.charAt(0);
				if(c==9 || c==32) {
					currentLine += s;
				} else {
					if(currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			if(!currentLine.isEmpty()) {
				headers.add(currentLine);
			}
			return headers;
		}	
	}
	
	/**
	 * Sends an error to client with specified code and message. 
	 * @param os output stream
	 * @param statusCode status code to set
	 * @param statusText status text to set
	 * @throws IOException throws if an exception occurred
	 */
	private static void sendError(OutputStream os, 
			int statusCode, String statusText) throws IOException {

		String response = "<html><head><title>"+statusText+"</title></head>"
					+ "<body><b>"+statusCode+" "+statusText+"</b></body><html>";

			os.write(("HTTP/1.1 "+statusCode+" "+statusText+"\r\n"+
					"Server: simple java server\r\n"+
					"Content-Type: text/html;charset=UTF-8\r\n"+
					"Content-Length: "+response.length()+"\r\n"+
					"Connection: close\r\n"+
					"\r\n"+response).getBytes(StandardCharsets.US_ASCII));
			os.flush();

		}
	
	
}

