package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection(){
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/clothify", "root", "12345");
        } catch (SQLException e) {
            throw new RuntimeException("Error establishing DB connection", e);
        }
    }

    public static DBConnection getInstance() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/clothify", "root", "12345");
        }
        return connection;
    }
}
