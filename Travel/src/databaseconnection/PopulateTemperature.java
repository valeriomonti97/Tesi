package databaseconnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

public class PopulateTemperature {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();

        try (Connection conn = db.connect()) {
            conn.setAutoCommit(false); // Inizia una transazione

            // Query per recuperare tutte le città
            String selectCittaSQL = "SELECT nome FROM citta";
            String selectVoliSQL = "SELECT giornopartenza, giornoarrivo FROM voli WHERE partenza = ? OR arrivo = ?";
            String insertTemperatureSQL = "INSERT INTO temperature (citta_id, data, temp_min, temp_max) " +
                    "VALUES ((SELECT id FROM citta WHERE nome = ?), ?, ?, ?) " +
                    "ON CONFLICT (citta_id, data) DO UPDATE " +
                    "SET temp_min = EXCLUDED.temp_min, temp_max = EXCLUDED.temp_max";

            // Prepara le query
            try (PreparedStatement pstmtCitta = conn.prepareStatement(selectCittaSQL);
                 PreparedStatement pstmtVoli = conn.prepareStatement(selectVoliSQL);
                 PreparedStatement pstmtInsert = conn.prepareStatement(insertTemperatureSQL);
                 ResultSet rsCitta = pstmtCitta.executeQuery()) {

                Random rand = new Random();

                // Itera su tutte le città
                while (rsCitta.next()) {
                    String cityName = rsCitta.getString("nome");

                    // Recupera le date di partenza e arrivo per ogni città
                    pstmtVoli.setString(1, cityName);
                    pstmtVoli.setString(2, cityName);

                    try (ResultSet rsVoli = pstmtVoli.executeQuery()) {
                        while (rsVoli.next()) {
                            LocalDate giornoPartenza = rsVoli.getDate("giornopartenza").toLocalDate();
                            LocalDate giornoArrivo = rsVoli.getDate("giornoarrivo").toLocalDate();

                            // Itera su tutte le date tra la partenza e l'arrivo
                            for (LocalDate date = giornoPartenza; !date.isAfter(giornoArrivo); date = date.plusDays(1)) {
                                // Genera temperature casuali
                                double tempMin = -10 + (rand.nextDouble() * 40); // Minimo tra -10°C e 30°C
                                double tempMax = tempMin + rand.nextDouble() * 10; // Massimo tra tempMin e tempMin + 10

                                // Inserisci le temperature nel database
                                pstmtInsert.setString(1, cityName);
                                pstmtInsert.setDate(2, Date.valueOf(date));
                                pstmtInsert.setDouble(3, tempMin);
                                pstmtInsert.setDouble(4, tempMax);
                                pstmtInsert.addBatch();
                            }
                        }

                        // Esegui il batch di inserimento
                        pstmtInsert.executeBatch();
                    }

                    System.out.println("Temperature generate per la città: " + cityName);
                }

                conn.commit(); // Conferma la transazione
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
