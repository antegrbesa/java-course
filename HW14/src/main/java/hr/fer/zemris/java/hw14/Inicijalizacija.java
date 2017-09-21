package hr.fer.zemris.java.hw14;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * This {@link WebListener} initializes connection-pool and creates (and fills with data) all necessary tables. 
 * It assumes that database whose name is given in properties file located at WEB-INF/dbsettings.properties already
 * exists. 
 * @author Ante Grbeša
 *
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {

	/**Path to configuration file*/
	private static final String CONFIG_FILE = "WEB-INF/dbsettings.properties";

	/**Part of a name of poll definition file*/
	private static final String DEFINITION_FILE = "-definition.txt";

	/**Part of a name of poll result file*/
	private static final String RESULT_FILE = "-results.txt";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String connectionURL = getArguments(sce);

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Error while initializing pool", e1);
		}
		cpds.setJdbcUrl(connectionURL);

		Connection con = null;
		try {
			con = cpds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("Error while connecting to database");
		}

		PreparedStatement pstFirst = null;
		PreparedStatement pstSecond = null;
		boolean tablesExist = false;
		try {
			pstFirst = con
					.prepareStatement("CREATE TABLE Polls (" + "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
							+ "title VARCHAR(150) NOT NULL, " + "message CLOB(2048) NOT NULL)");

			pstFirst.executeUpdate();

			pstSecond = con.prepareStatement(
					"CREATE TABLE PollOptions (" + "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
							+ "optionTitle VARCHAR(100) NOT NULL," + "optionLink VARCHAR(150) NOT NULL,"
							+ "pollID BIGINT," + "votesCount BIGINT," + "FOREIGN KEY (pollID) REFERENCES Polls(id))");

			pstSecond.executeUpdate();
		} catch (SQLException e) {
			if (Util.tableAlreadyExists(e)) {
				tablesExist = true;
			} else {
				throw new RuntimeException("Error while creating tables", e);
			}
		}

		if (tablesExist) {
			checkTables(con, sce.getServletContext());
		} else {
			populatePolls(con, sce.getServletContext());			
		}

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
	}

	/**
	 * Checks if tables Polls and PollsOptions are empty. If any of the tables 
	 * is empty, they are populated with default values. 
	 * @param con connection
	 * @param servletContext ServletContext
	 */
	private void checkTables(Connection con, ServletContext servletContext) {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("SELECT COUNT(*)  FROM Polls");
			ResultSet rset = pst.executeQuery();
			rset.next();
			long res = rset.getLong(1);
			if (res == 0) {
				populatePolls(con, servletContext);
				return;
			}

			pst = con.prepareStatement("SELECT COUNT(*) FROM PollOptions");
			ResultSet rset2 = pst.executeQuery();
			rset2.next();
			long res2 = rset2.getLong(1);
			if (res2 == 0) {
				populatePolls(con, servletContext);
			}

		} catch (Exception e) {
			throw new RuntimeException("Exception occurred while reading from database: polls", e);
		}

	}

	/**
	 * Populates pollsOptions table with values defined in text file at location: 
	 * {@code WEB-INF/id-definition.txt} and {@code WEB-INF/id-results.txt}.
	 * @param con Connection
	 * @param servletContext ServletContext
	 * @param id file id of poll (different from id generated in polls table)
	 * @param genKeys ResultSet
	 * @throws IOException if exception occurs
	 */
	private void populateOption(Connection con, ServletContext servletContext, String id, ResultSet genKeys)
			throws IOException {
		try {
			String def = servletContext.getRealPath("WEB-INF/" + id + DEFINITION_FILE);
			String res = servletContext.getRealPath("WEB-INF/" + id + RESULT_FILE);

			Map<String, String> valuesID = Util.getValues(def, 0, 1); //keys are ids, values are option titles
			Map<String, String> valuesName = Util.getValues(def, 1, 2); //keys are option titles, values are links
			Map<String, Integer> results = Util.getResultingMap(res, def); //keys are option titles, values are number of votes

			for (Map.Entry<String, String> entry : valuesID.entrySet()) {
				PreparedStatement stat = null;

				stat = con.prepareStatement(
						"INSERT INTO PollOptions (optionTitle, optionLink, pollId, votesCount) values (?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				stat.setString(1, entry.getValue());
				stat.setString(2, valuesName.get(entry.getValue()));
				stat.setLong(3, genKeys.getLong(1));
				stat.setLong(4, results.get(entry.getValue()));

				stat.executeUpdate();
			}

		} catch (SQLException ex) {
			throw new RuntimeException("Error while adding values to database", ex);
		}
	}

	/**
	 * Populates polls and pollOptions tables.
	 * @param con Connection
	 * @param servletContext ServletContext
	 */
	private void populatePolls(Connection con, ServletContext servletContext) {
		String fileName = servletContext.getRealPath("WEB-INF/polls.txt");
		Map<String, String> valuesID = null;
		Map<String, String> valuesName = null;
		try {
			valuesID = Util.getValues(fileName, 0, 1);
			valuesName = Util.getValues(fileName, 1, 2);
		} catch (IOException e) {
			throw new RuntimeException("Error occurred while reading data from file.", e);
		}

		ResultSet genKeys = null;
		for (Map.Entry<String, String> e : valuesID.entrySet()) {
			String title = e.getValue();
			String message = valuesName.get(title);

			PreparedStatement pst = null;

			try {
				pst = con.prepareStatement("INSERT INTO Polls (title, message) values (?,?)",
						Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, title);
				pst.setString(2, message);

				pst.executeUpdate();
				genKeys = pst.getGeneratedKeys();
				genKeys.next();

				populateOption(con, servletContext, e.getKey(), genKeys);
			} catch (SQLException | IOException ex) {
				throw new RuntimeException("Error while adding values to database", ex);
			}

		}

	}

	/**
	 * Gets the connection URL created from properties file. 
	 * @param sce ServletContextEvent
	 * @return  connection URL 
	 */
	private String getArguments(ServletContextEvent sce) {
		Properties properties = new Properties();
		String configFileName = sce.getServletContext().getRealPath(CONFIG_FILE);
		try {
			properties.load(Files.newInputStream(Paths.get(configFileName)));
		} catch (IOException e) {
			throw new RuntimeException("Invalid config file, was " + configFileName);
		}

		String[] arguments = new String[2];
		arguments[0] = properties.getProperty("name");
		arguments[1] = getURL(properties, arguments[0]);

		return arguments[1];
	}

	/**
	 * Generates URL from given properties.
	 * @param properties Properties
	 * @param dbName database name
	 * @return generated url
	 */
	private String getURL(Properties properties, String dbName) {
		StringBuilder sb = new StringBuilder();
		sb.append("jdbc:derby://");
		sb.append(properties.getProperty("host"));
		sb.append(":");
		sb.append(properties.getProperty("port"));
		sb.append("/");
		sb.append(dbName);
		sb.append(";user=");
		sb.append(properties.getProperty("user"));
		sb.append(";password=");
		sb.append(properties.getProperty("password"));

		return sb.toString();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext()
				.getAttribute("hr.fer.zemris.dbpool");
		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}