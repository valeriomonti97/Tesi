package databaseconnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class PopulateContinentiECitta {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        String pathContinenti = "C:\\Users\\valer\\Documents\\Tesi\\ProgettoLinguaggiETecnologiePerIlWeb-master\\Documenti\\continenti.csv"; // Modifica con il percorso corretto
        String pathCitta = "C:\\Users\\valer\\Documents\\Tesi\\ProgettoLinguaggiETecnologiePerIlWeb-master\\Documenti\\citta.csv"; // Modifica con il percorso corretto

        try (Connection conn = db.connect()) {
            conn.setAutoCommit(false); // Inizia la transazione

            // Popolare Continenti
            String insertContinente = "INSERT INTO continenti (nome) VALUES (?) ON CONFLICT (nome) DO NOTHING;";
            try (PreparedStatement pstmtContinente = conn.prepareStatement(insertContinente)) {
                BufferedReader br = new BufferedReader(new FileReader(pathContinenti));
                String line;
                br.readLine(); // Salta l'intestazione
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

            // Popolare Città
            String insertCitta = "INSERT INTO citta (nome, continente_id, paese, latitudine, longitudine) " +
                                 "VALUES (?, (SELECT id FROM continenti WHERE nome = ?), ?, ?, ?) " +
                                 "ON CONFLICT (nome) DO NOTHING;";
            try (PreparedStatement pstmtCitta = conn.prepareStatement(insertCitta)) {
                BufferedReader br = new BufferedReader(new FileReader(pathCitta));
                String line;
                br.readLine(); // Salta l'intestazione
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 5) {
                        try {
                            pstmtCitta.setString(1, data[0].trim()); // nome
                            pstmtCitta.setString(2, data[1].trim()); // continente
                            pstmtCitta.setString(3, data[2].trim()); // paese

                            // Controllo se latitudine e longitudine sono numeriche
                            double latitudine = parseDoubleSafe(data[3].trim());
                            double longitudine = parseDoubleSafe(data[4].trim());

                            pstmtCitta.setDouble(4, latitudine); // latitudine
                            pstmtCitta.setDouble(5, longitudine); // longitudine

                            pstmtCitta.addBatch();
                        } catch (NumberFormatException e) {
                            // Gestisci il caso in cui la latitudine o longitudine non siano valide
                            System.err.println("Errore nei dati di città: " + data[0] + ". Latitudine o longitudine non valide.");
                        }
                    }
                }
                pstmtCitta.executeBatch();
                br.close();
            }

            conn.commit(); // Conferma la transazione
            System.out.println("Continenti e città popolate con successo.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // Funzione per gestire i valori numerici
    private static double parseDoubleSafe(String value) throws NumberFormatException {
        if (value == null || value.isEmpty()) {
            throw new NumberFormatException("Valore vuoto o nullo.");
        }
        return Double.parseDouble(value);
    }
}