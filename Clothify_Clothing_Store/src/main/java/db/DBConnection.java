package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection(){
        try {
            this.connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/Clothify","root","12345");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DBConnection getInstance(){
        return dbConnection==null ? dbConnection=new DBConnection() : dbConnection;
    }
}
