package databaseconnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTables {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        String[] tableQueries = {
            // Tabella Continenti
            "CREATE TABLE IF NOT EXISTS continenti (" +
            "id SERIAL PRIMARY KEY," +
            "nome VARCHAR(50) NOT NULL UNIQUE" +
            ");",
            
            // Tabella Città
            "CREATE TABLE IF NOT EXISTS citta (" +
            "id SERIAL PRIMARY KEY," +
            "nome VARCHAR(100) NOT NULL," +
            "continente_id INTEGER REFERENCES continenti(id)," +
            "paese VARCHAR(100) NOT NULL," +
            "latitudine DOUBLE PRECISION," +
            "longitudine DOUBLE PRECISION" +
            ");",
            
            // Tabella Temperature
            "CREATE TABLE IF NOT EXISTS temperature (" +
            "id SERIAL PRIMARY KEY," +
            "citta_id INTEGER REFERENCES citta(id)," +
            "data DATE NOT NULL," +
            "temp_min FLOAT," +
            "temp_max FLOAT" +
            ");",
            
            // Tabella Compagnie Aeree
            "CREATE TABLE IF NOT EXISTS compagnie_aeree (" +
            "id SERIAL PRIMARY KEY," +
            "nome VARCHAR(100) NOT NULL UNIQUE" +
            ");",
            
            // Tabella Tratte di Volo
            "CREATE TABLE IF NOT EXISTS tratte_volo (" +
            "id SERIAL PRIMARY KEY," +
            "compagnia_id INTEGER REFERENCES compagnie_aeree(id)," +
            "citta_partenza_id INTEGER REFERENCES citta(id)," +
            "citta_destinazione_id INTEGER REFERENCES citta(id)," +
            "orario_partenza TIME," +
            "orario_arrivo TIME" +
            ");"
        };

        try (Connection conn = db.connect();
             Statement stmt = conn.createStatement()) {
             
            for (String query : tableQueries) {
                stmt.execute(query);
            }
            System.out.println("Tabelle create con successo.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

