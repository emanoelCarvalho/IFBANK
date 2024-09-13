package projeto.banco.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import projeto.banco.utils.ConfigLoader;

public class ConexaoMySql implements IConnection {
	private final String DB_USER;
	private final String DB_PASS;
	private final String DB_PORT;
	private final String DB_HOST;
	private final String DB_NAME;

	private Connection connection;

	{
		DB_USER = ConfigLoader.getInstance("config").getProperty("DB_USER");
		DB_PASS = System.getenv("MYSQL_PASSWORD");
		DB_PORT = ConfigLoader.getInstance("config").getProperty("DB_PORT");
		DB_HOST = ConfigLoader.getInstance("config").getProperty("DB_HOST");
		DB_NAME = ConfigLoader.getInstance("config").getProperty("DB_NAME");
	}

	@Override
	public Connection getConnection() {
		if (connection == null) {
			try {
				connection = DriverManager.getConnection("jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME,
						DB_USER, DB_PASS);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return connection;
	}

	@Override
	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
				connection = null; 
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}
