package databaseconnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PopulateVoli {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        
        // Lista di compagnie aeree reali
        String[] airlines = {
            "American Airlines", "British Airways", "Delta Air Lines", 
            "United Airlines", "Air France", "Lufthansa", 
            "Emirates", "Qantas", "Singapore Airlines", "Ryanair", "ITA Airlines"
        };
        
        Random rand = new Random();

        try (Connection conn = db.connect()) {
            conn.setAutoCommit(false); // Inizia la transazione

            // Recupera tutte le città dal database
            List<String[]> cities = new ArrayList<>();
            String selectCittaSQL = "SELECT nome FROM citta";
            try (PreparedStatement pstmt = conn.prepareStatement(selectCittaSQL);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    cities.add(new String[]{rs.getString("nome")});
                }
            }

            // Se ci sono meno di due città, non possiamo creare voli
            if (cities.size() < 2) {
                throw new IllegalStateException("Non ci sono abbastanza città per creare voli.");
            }

            // Popolare la tabella dei voli
            String insertFlightSQL = "INSERT INTO voli (codice, compagnia, partenza, arrivo, giornopartenza, giornoarrivo, prezzo) "
                                    + "VALUES (?, ?, ?, ?, ?, ?, ?) ON CONFLICT (codice) DO NOTHING";
            try (PreparedStatement pstmtFlight = conn.prepareStatement(insertFlightSQL)) {

                // 1. Crea almeno un volo per ogni città
                for (String[] departureCity : cities) {
                    String[] arrivalCity;
                    do {
                        arrivalCity = cities.get(rand.nextInt(cities.size()));
                    } while (departureCity[0].equals(arrivalCity[0])); // Assicurati che partenza e arrivo siano diverse

                    // Seleziona una compagnia aerea casuale
                    String airline = airlines[rand.nextInt(airlines.length)];

                    // Genera un codice volo casuale
                    String flightCode = airline.substring(0, 2).toUpperCase() + String.format("%04d", rand.nextInt(10000));

                    // Genera una data di partenza casuale nei prossimi 3 mesi
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime departureDateTime = now.plus(rand.nextInt(90), ChronoUnit.DAYS)
                                                         .plus(rand.nextInt(24), ChronoUnit.HOURS)
                                                         .plus(rand.nextInt(60), ChronoUnit.MINUTES);

                    // Durata del volo casuale tra 1 e 15 ore
                    int flightDuration = rand.nextInt(15) + 1;
                    LocalDateTime arrivalDateTime = departureDateTime.plusHours(flightDuration);

                    // Prezzo casuale tra 50 e 1000 euro
                    double price = 50 + (950 * rand.nextDouble());

                    // Inserimento nel batch
                    pstmtFlight.setString(1, flightCode); // codice
                    pstmtFlight.setString(2, airline); // compagnia
                    pstmtFlight.setString(3, departureCity[0]); // partenza (nome della città)
                    pstmtFlight.setString(4, arrivalCity[0]); // arrivo (nome della città)
                    pstmtFlight.setTimestamp(5, Timestamp.valueOf(departureDateTime)); // giornopartenza
                    pstmtFlight.setTimestamp(6, Timestamp.valueOf(arrivalDateTime)); // giornoarrivo
                    pstmtFlight.setDouble(7, price); // prezzo
                    pstmtFlight.addBatch();
                }

                // 2. Genera voli casuali aggiuntivi
                for (int i = 0; i < 1000000; i++) { // Genera 100 voli casuali
                    String[] departureCity = cities.get(rand.nextInt(cities.size()));
                    String[] arrivalCity;
                    do {
                        arrivalCity = cities.get(rand.nextInt(cities.size()));
                    } while (departureCity[0].equals(arrivalCity[0])); // Assicurati che partenza e arrivo siano diverse

                    // Seleziona una compagnia aerea casuale
                    String airline = airlines[rand.nextInt(airlines.length)];

                    // Genera un codice volo casuale
                    String flightCode = airline.substring(0, 2).toUpperCase() + String.format("%04d", rand.nextInt(10000));

                    // Genera una data di partenza casuale nei prossimi 3 mesi
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime departureDateTime = now.plus(rand.nextInt(90), ChronoUnit.DAYS)
                                                         .plus(rand.nextInt(24), ChronoUnit.HOURS)
                                                         .plus(rand.nextInt(60), ChronoUnit.MINUTES);

                    // Durata del volo casuale tra 1 e 15 ore
                    int flightDuration = rand.nextInt(15) + 1;
                    LocalDateTime arrivalDateTime = departureDateTime.plusHours(flightDuration);

                    // Prezzo casuale tra 50 e 1000 euro
                    double price = 50 + (950 * rand.nextDouble());

                    // Inserimento nel batch
                    pstmtFlight.setString(1, flightCode); // codice
                    pstmtFlight.setString(2, airline); // compagnia
                    pstmtFlight.setString(3, departureCity[0]); // partenza (nome della città)
                    pstmtFlight.setString(4, arrivalCity[0]); // arrivo (nome della città)
                    pstmtFlight.setTimestamp(5, Timestamp.valueOf(departureDateTime)); // giornopartenza
                    pstmtFlight.setTimestamp(6, Timestamp.valueOf(arrivalDateTime)); // giornoarrivo
                    pstmtFlight.setDouble(7, price); // prezzo
                    pstmtFlight.addBatch();
                }

                // Esegui il batch
                pstmtFlight.executeBatch();
            }

            conn.commit(); // Conferma la transazione
            System.out.println("Voli popolati con successo.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
