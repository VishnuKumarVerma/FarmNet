package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/farmnet";
    private static final String USER = "root";
    private static final String PASSWORD = "dbPassCode";
    
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
