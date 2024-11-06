package databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final String url = "jdbc:postgresql://localhost:5432/travel_db"; 
    private final String user = "postgres"; 
    private final String password = "****"; 

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    
}
