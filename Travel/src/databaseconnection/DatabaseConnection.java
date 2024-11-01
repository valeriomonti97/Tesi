package databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final String url = "jdbc:postgresql://localhost:5432/travel_db"; // Modifica se necessario
    private final String user = "postgres"; // Sostituisci con il tuo utente
    private final String password = "210597vm"; // Sostituisci con la tua password

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    
}
