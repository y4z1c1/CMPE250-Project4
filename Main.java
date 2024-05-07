
/**
 * The main class for managing an airline's flight data processing application.
 * This class handles the reading of airport, weather, flight directions, and mission data from files.
 * It initializes an Airline instance and populates it with the read data to simulate flight operations.
* @author Yusuf Anil Yazici

 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    // File names for various data inputs
    private static String AIRPORTS_FILE;
    private static String WEATHER_FILE;
    private static String DIRECTIONS_FILE;
    private static String MISSION_FILE;
    private static String outputFile;
    private static String outputFile2;

    // The airline instance for managing flight data
    private static Airline airline;

    /**
     * The main method to run the airline simulation.
     * It initializes file reading and processes data to simulate airline
     * operations.
     * 
     * @param args Command-line arguments for file names.
     */
    public static void main(String[] args) {
        // File path initialization from args

        args[0] = AIRPORTS_FILE;
        args[1] = DIRECTIONS_FILE;
        args[2] = WEATHER_FILE;
        args[3] = AIRPORTS_FILE;
        args[4] = outputFile;
        args[5] = outputFile2;

        airline = new Airline();

        try {

            FileWrite.initWriter(outputFile);

            // Reading data from files and populating the airline instance
            readAirports();
            readWeather();
            readFlightDirections();
            readAndProcessMissions(MISSION_FILE);

        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            FileWrite.closeWriter();
        }

    }

    /**
     * Reads airports data from a file and adds them to the airline.
     * 
     * @throws IOException If an error occurs during file reading.
     */
    private static void readAirports() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(AIRPORTS_FILE))) {
            String line;
            br.readLine(); // Skip the header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String airportCode = values[0];
                String airfieldName = values[1];
                double latitude = Double.parseDouble(values[2]);
                double longitude = Double.parseDouble(values[3]);
                double parkingCost = Double.parseDouble(values[4]);

                Airport airport = new Airport(airportCode, airfieldName, latitude, longitude, parkingCost);
                airline.addAirport(airport);
            }
        }
    }

    /**
     * Reads weather data from a file and adds weather conditions to airfields.
     * 
     * @throws IOException If an error occurs during file reading.
     */
    private static void readWeather() throws IOException {
        Map<String, Airfield> airfieldMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(WEATHER_FILE))) {
            String line;
            br.readLine(); // Skip the header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String airfieldName = values[0];
                long time = Long.parseLong(values[1]);
                int weatherCode = Integer.parseInt(values[2]);

                Airfield airfield = airfieldMap.computeIfAbsent(airfieldName, k -> new Airfield(k));
                airfield.addWeatherCondition(time, weatherCode);
            }
        }

        // Add all airfields to the airline
        for (Airfield airfield : airfieldMap.values()) {
            airline.addAirfield(airfield);
        }
    }

    /**
     * Reads flight directions data from a file and adds flight paths to the
     * airline.
     * 
     * @throws IOException If an error occurs during file reading.
     */
    private static void readFlightDirections() throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(DIRECTIONS_FILE))) {
            String line;
            br.readLine(); // Skip the header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                // Get 'from' and 'to' airport codes from the line
                String fromCode = values[0];
                String toCode = values[1];

                // Retrieve the corresponding Airport objects
                Airport fromAirport = airline.getAirport(fromCode);
                Airport toAirport = airline.getAirport(toCode);

                // Check if both airports exist before adding the flight direction
                if (fromAirport != null && toAirport != null) {
                    airline.addFlightDirection(fromAirport, toAirport);
                } else {
                    // Handle the case where airports are not found in the airline's data
                    System.out.println("Airport not found for direction: " + fromCode + " -> " + toCode);
                }
            }
        }
    }

    /**
     * Reads and processes missions from a file, simulating flight operations.
     * 
     * @param missionFile The file containing mission data.
     * @throws IOException If an error occurs during file reading.
     */
    private static void readAndProcessMissions(String missionFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(missionFile))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                // Assuming each line contains fromCode, toCode, and time separated by spaces
                String[] values = line.split(" ");
                String fromCode = values[0];
                String toCode = values[1];
                long time = Long.parseLong(values[2]);

                // Process each mission
                processMission(fromCode, toCode, time);
            }
        }
    }

    /**
     * Processes a single flight mission, finding the path and calculating the cost.
     * 
     * @param fromCode Airport code of the origin.
     * @param toCode   Airport code of the destination.
     * @param time     Time of the flight for weather considerations.
     */
    private static void processMission(String fromCode, String toCode, long time) {
        Airport fromAirport = airline.getAirport(fromCode);
        Airport toAirport = airline.getAirport(toCode);

        if (fromAirport == null || toAirport == null) {
            System.out.println("Invalid airport code(s) in mission: " + fromCode + " -> " + toCode);
            return;
        }

        List<Airport> path = airline.findSuccessiveFlights(fromAirport, toAirport, time);
        if (path.isEmpty()) {
            System.out.println("No path found from " + fromCode + " to " + toCode);
        } else {
            double totalCost = 0;
            String output = "";
            for (int i = 0; i < path.size(); i++) {
                Airport airport = path.get(i);
                System.out.print(airport.getAirportCode() + " ");
                output += airport.getAirportCode() + " ";
                if (i < path.size() - 1) {
                    totalCost += airline.getFlightCost(airport, path.get(i + 1), time);
                }
            }
            System.out.println(String.format(" %.5f", totalCost));
            output += String.format("%.5f", totalCost);
            FileWrite.writeToFile(output);
        }
    }

    // Other methods for file reading and processing...
}
