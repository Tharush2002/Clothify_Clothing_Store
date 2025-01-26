package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;

    // Private constructor for singleton pattern
    private DBConnection(){
        try {
            // Make sure the connection is established only once
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/clothify", "root", "12345");
        } catch (SQLException e) {
            throw new RuntimeException("Error establishing DB connection", e);
        }
    }

    // Public method to get the singleton instance
    public static DBConnection getInstance() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    // Method to get a connection, checking if the connection is closed
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Reconnect if the connection is closed or null
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/clothify", "root", "12345");
        }
        return connection;
    }
}
