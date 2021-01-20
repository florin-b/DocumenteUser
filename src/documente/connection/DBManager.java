package documente.connection;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

public class DBManager {

	public Connection getConnection() {

		Connection connection = null;

		Properties props = getDBProperties();

		String url = props.getProperty("db_engine") + "://" + props.getProperty("db_ip") + "/"
				+ props.getProperty("db_name");

		Properties connProps = new Properties();
		connProps.setProperty("user", props.getProperty("db_user"));
		connProps.setProperty("password", props.getProperty("db_pass"));

		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, connProps);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return connection;
	}
	
	
	public DataSource getProdDataSource() {

		OracleDataSource oracleDS = null;
		try {

			oracleDS = new OracleDataSource();
			oracleDS.setURL("jdbc:oracle:thin:@10.1.3.76:1521/PRD");
			oracleDS.setUser("WEBSAP");
			oracleDS.setPassword("2INTER7");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return oracleDS;
	}

	private Properties getDBProperties() {
		Properties props = new Properties();
		String propFile = "resources/config.properties";

		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFile);

			if (inputStream != null) {
				props.load(inputStream);
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return props;
	}

}
