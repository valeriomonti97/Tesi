package databaseconnection;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;


public class PopulateAirports {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        String pathAirports = "C:\\Users\\valer\\Downloads\\airports4.dat"; // Modifica con il percorso corretto

        try (Connection conn = db.connect()) {
            conn.setAutoCommit(false); // Disattiva l'auto-commit per gestire manualmente
            
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT to_regclass('public.airports')");
                if (rs.next() && rs.getString(1) == null) {
                    System.out.println("La tabella airports non esiste.");
                    return;
                }
            }
            
            String insertAirport = "INSERT INTO airports (nome, città, paese, fuso_orario, tz_database) " +
                    "VALUES (?, ?, ?, ?, ?)";
                    

            try (CSVReader reader = new CSVReader(new FileReader(pathAirports));
                 PreparedStatement pstmt = conn.prepareStatement(insertAirport)) {

                String[] line;
                while ((line = reader.readNext()) != null) {
                	 System.out.println("Riga letta: " + Arrays.toString(line) + line.length);
                    if (line.length >= 5) {
                    	System.out.println("OK");
                        try {
                            pstmt.setString(1, handleNull(line[0])); // Nome
                            pstmt.setString(2, handleNull(line[1])); // Città
                            pstmt.setString(3, handleNull(line[2])); // Paese
                            double fusoOrario;
                            String tzDatabase;

                            // Parsing dei campi numerici
                            if (line[3].isEmpty()) {
                            	fusoOrario = 0.0;
                            } 
                            else {
                            	fusoOrario = handleNumericNull(line[3], 0.0);
                            	} // Fuso Orario
                           
                            if (line[4].isEmpty()) {
                            	
                            	tzDatabase= "";
                            	
                            } else {
                            	tzDatabase=handleNull(line[4]);} // TZ Database
                            
                            
                         // Gestisci valori vuoti o null
                            pstmt.setDouble(4, fusoOrario);
                            pstmt.setString(5, tzDatabase);
                            pstmt.addBatch();  // Aggiunge al batch
                            
                           

                        } catch (NumberFormatException e) {
                            System.out.println("Errore di parsing per la riga: " + String.join(",", line));
                            e.printStackTrace();
                        }
                    }
                }
                pstmt.executeBatch(); // Esegui il batch di inserimenti
                try {
                    // codice che esegue le operazioni di database
                    conn.commit();
                    System.out.println("Dati inseriti correttamente e commit eseguito.");
                } catch (SQLException e) {
                    e.printStackTrace();  // Questo mostrerà se ci sono problemi
                    System.out.println("Errore nell'inserimento dei dati: " + e.getMessage());
                }

                System.out.println("Aeroporti popolati con successo.");
            } catch (IOException | CsvValidationException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Funzione per gestire i campi non numerici
    public static String handleNull(String value) {
        return value.equals("\\N") ? null : value.replaceAll("\"", "").trim();
    }

    // Funzione per gestire i campi numerici
    public static double handleNumericNull(String value, double defaultValue) {
        return value.equals("\\N") ? defaultValue : Double.parseDouble(value.replaceAll("\"", "").trim());
    }

    public static int handleNumericNull(String value, int defaultValue) {
        return value.equals("\\N") ? defaultValue : Integer.parseInt(value.replaceAll("\"", "").trim());
    }
}
