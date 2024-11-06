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

        String[] airlines = {
            "American Airlines", "British Airways", "Delta Air Lines", 
            "United Airlines", "Air France", "Lufthansa", 
            "Emirates", "Qantas", "Singapore Airlines", "Ryanair", "ITA Airlines"
        };
        
        Random rand = new Random();

        try (Connection conn = db.connect()) {
            conn.setAutoCommit(false);

            List<String[]> cities = new ArrayList<>();
            String selectCittaSQL = "SELECT nome FROM citta";
            try (PreparedStatement pstmt = conn.prepareStatement(selectCittaSQL);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    cities.add(new String[]{rs.getString("nome")});
                }
            }

            if (cities.size() < 2) {
                throw new IllegalStateException("Non ci sono abbastanza città per creare voli.");
            }

            String insertFlightSQL = "INSERT INTO voli (codice, compagnia, partenza, arrivo, giornopartenza, giornoarrivo, prezzo) "
                                    + "VALUES (?, ?, ?, ?, ?, ?, ?) ON CONFLICT (codice) DO NOTHING";
            try (PreparedStatement pstmtFlight = conn.prepareStatement(insertFlightSQL)) {

                for (String[] departureCity : cities) {
                    String[] arrivalCity;
                    do {
                        arrivalCity = cities.get(rand.nextInt(cities.size()));
                    } while (departureCity[0].equals(arrivalCity[0])); 

                    String airline = airlines[rand.nextInt(airlines.length)];

                    String flightCode = airline.substring(0, 2).toUpperCase() + String.format("%04d", rand.nextInt(10000));

                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime departureDateTime = now.plus(rand.nextInt(90), ChronoUnit.DAYS)
                                                         .plus(rand.nextInt(24), ChronoUnit.HOURS)
                                                         .plus(rand.nextInt(60), ChronoUnit.MINUTES);

                    int flightDuration = rand.nextInt(15) + 1;
                    LocalDateTime arrivalDateTime = departureDateTime.plusHours(flightDuration);

                    double price = 50 + (950 * rand.nextDouble());

                    pstmtFlight.setString(1, flightCode); 
                    pstmtFlight.setString(2, airline); 
                    pstmtFlight.setString(3, departureCity[0]); 
                    pstmtFlight.setString(4, arrivalCity[0]);
                    pstmtFlight.setTimestamp(5, Timestamp.valueOf(departureDateTime));
                    pstmtFlight.setTimestamp(6, Timestamp.valueOf(arrivalDateTime)); 
                    pstmtFlight.setDouble(7, price); 
                    pstmtFlight.addBatch();
                }

                for (int i = 0; i < 1000000; i++) {
                    String[] departureCity = cities.get(rand.nextInt(cities.size()));
                    String[] arrivalCity;
                    do {
                        arrivalCity = cities.get(rand.nextInt(cities.size()));
                    } while (departureCity[0].equals(arrivalCity[0])); 

                    String airline = airlines[rand.nextInt(airlines.length)];

                    String flightCode = airline.substring(0, 2).toUpperCase() + String.format("%04d", rand.nextInt(10000));

                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime departureDateTime = now.plus(rand.nextInt(90), ChronoUnit.DAYS)
                                                         .plus(rand.nextInt(24), ChronoUnit.HOURS)
                                                         .plus(rand.nextInt(60), ChronoUnit.MINUTES);

                    int flightDuration = rand.nextInt(15) + 1;
                    LocalDateTime arrivalDateTime = departureDateTime.plusHours(flightDuration);

                    double price = 50 + (950 * rand.nextDouble());

                    pstmtFlight.setString(1, flightCode); 
                    pstmtFlight.setString(2, airline); 
                    pstmtFlight.setString(3, departureCity[0]);
                    pstmtFlight.setString(4, arrivalCity[0]); 
                    pstmtFlight.setTimestamp(5, Timestamp.valueOf(departureDateTime)); 
                    pstmtFlight.setTimestamp(6, Timestamp.valueOf(arrivalDateTime)); 
                    pstmtFlight.setDouble(7, price); 
                    pstmtFlight.addBatch();
                }

                pstmtFlight.executeBatch();
            }

            conn.commit(); 
            System.out.println("Voli popolati con successo.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
