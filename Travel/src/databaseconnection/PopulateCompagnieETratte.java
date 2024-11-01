package databaseconnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class PopulateCompagnieETratte {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        String pathAirlines = "C:\\Users\\valer\\Downloads\\airlines.dat"; // Modifica con il percorso corretto
        String pathRoutes = "C:\\Users\\valer\\Downloads\\routes.dat"; // Modifica con il percorso corretto

        try (Connection conn = db.connect()) {
            conn.setAutoCommit(false);

            // Popolare Compagnie Aeree
            String insertCompagnia = "INSERT INTO compagnie_aeree (nome) VALUES (?) ON CONFLICT (nome) DO NOTHING;";
            try (BufferedReader br = new BufferedReader(new FileReader(pathAirlines));
                 PreparedStatement pstmtComp = conn.prepareStatement(insertCompagnia)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 2) {
                        String nomeCompagnia = data[1].replaceAll("\"", "").trim();
                        if (!nomeCompagnia.isEmpty()) {
                            pstmtComp.setString(1, nomeCompagnia);
                            pstmtComp.addBatch();
                        }
                    }
                }
                pstmtComp.executeBatch();
            }

            // Popolare Tratte di Volo
            String insertTratta = "INSERT INTO tratte_volo (compagnia_id, citta_partenza_id, citta_destinazione_id, orario_partenza, orario_arrivo) " +
                                  "VALUES (" +
                                  "(SELECT id FROM compagnie_aeree WHERE nome = ? LIMIT 1), " +
                                  "(SELECT c.id FROM citta c JOIN airports a ON c.nome = a.città WHERE a.iata = ? LIMIT 1), " +
                                  "(SELECT c.id FROM citta c JOIN airports a ON c.nome = a.città WHERE a.iata = ? LIMIT 1), " +
                                  "NULL, NULL" + // Puoi aggiungere orari se disponibili
                                  ") " +
                                  "ON CONFLICT DO NOTHING;";
            
            // Nota: Questo esempio presuppone che tu abbia una tabella `airports` e una tabella `citta` correttamente popolata.

            try (BufferedReader br = new BufferedReader(new FileReader(pathRoutes));
                 PreparedStatement pstmtTratta = conn.prepareStatement(insertTratta)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 6) {
                        String airline = data[1].replaceAll("\"", "").trim();
                        String sourceAirport = data[3].replaceAll("\"", "").trim();
                        String destAirport = data[5].replaceAll("\"", "").trim();

                        if (!airline.isEmpty() && !sourceAirport.isEmpty() && !destAirport.isEmpty()) {
                            pstmtTratta.setString(1, airline);
                            pstmtTratta.setString(2, sourceAirport);
                            pstmtTratta.setString(3, destAirport);
                            pstmtTratta.addBatch();
                        }
                    }
                }
                pstmtTratta.executeBatch();
            }

            conn.commit();
            System.out.println("Compagnie aeree e tratte di volo popolate con successo.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}

