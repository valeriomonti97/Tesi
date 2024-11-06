package databaseconnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class PopulateContinentiECitta {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        String pathContinenti = "C:\\Users\\valer\\Documents\\Tesi\\ProgettoLinguaggiETecnologiePerIlWeb-master\\Documenti\\continenti.csv"; 
        String pathCitta = "C:\\Users\\valer\\Documents\\Tesi\\ProgettoLinguaggiETecnologiePerIlWeb-master\\Documenti\\citta.csv"; 

        try (Connection conn = db.connect()) {
            conn.setAutoCommit(false); 

            // Popolare Continenti
            String insertContinente = "INSERT INTO continenti (nome) VALUES (?) ON CONFLICT (nome) DO NOTHING;";
            try (PreparedStatement pstmtContinente = conn.prepareStatement(insertContinente)) {
                BufferedReader br = new BufferedReader(new FileReader(pathContinenti));
                String line;
                br.readLine(); 
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length > 0) {
                        pstmtContinente.setString(1, data[0].trim());
                        pstmtContinente.addBatch();
                    }
                }
                pstmtContinente.executeBatch();
                br.close();
            }

            
            String insertCitta = "INSERT INTO citta (nome, continente_id, paese, latitudine, longitudine) " +
                                 "VALUES (?, (SELECT id FROM continenti WHERE nome = ?), ?, ?, ?) " +
                                 "ON CONFLICT (nome) DO NOTHING;";
            try (PreparedStatement pstmtCitta = conn.prepareStatement(insertCitta)) {
                BufferedReader br = new BufferedReader(new FileReader(pathCitta));
                String line;
                br.readLine(); 
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 5) {
                        try {
                            pstmtCitta.setString(1, data[0].trim()); 
                            pstmtCitta.setString(2, data[1].trim()); 
                            pstmtCitta.setString(3, data[2].trim()); 

                            double latitudine = parseDoubleSafe(data[3].trim());
                            double longitudine = parseDoubleSafe(data[4].trim());

                            pstmtCitta.setDouble(4, latitudine); 
                            pstmtCitta.setDouble(5, longitudine); 

                            pstmtCitta.addBatch();
                        } catch (NumberFormatException e) {
                            System.err.println("Errore nei dati di città: " + data[0] + ". Latitudine o longitudine non valide.");
                        }
                    }
                }
                pstmtCitta.executeBatch();
                br.close();
            }

            conn.commit(); 
            System.out.println("Continenti e città popolate con successo.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static double parseDoubleSafe(String value) throws NumberFormatException {
        if (value == null || value.isEmpty()) {
            throw new NumberFormatException("Valore vuoto o nullo.");
        }
        return Double.parseDouble(value);
    }
}