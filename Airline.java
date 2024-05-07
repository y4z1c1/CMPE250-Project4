/**
 * Represents an airline with its network of flights and airports.
 * This class handles the creation and management of flight routes between airports,
 * as well as tracking airfields associated with these airports.
 * It offers methods for adding airports and airfields, managing flights, and calculating
 * flight paths and costs.
 * @author Yusuf Anil Yazici
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


public class Airline {
    // Maps each airport to a list of airports to which it has direct flights
    private Map<Airport, List<Airport>> flightMap;

    // Stores airfields by their names
    private Map<String, Airfield> airfields;

    // Stores airports by their unique codes
    private Map<String, Airport> airports;

    /**
     * Constructor to initialize the Airline with empty maps for flight routes, airfields, and airports.
     */
    public Airline() {
        flightMap = new HashMap<>();
        airfields = new HashMap<>();
        airports = new HashMap<>();
    }

    /**
     * Adds an airport to the airline's network.
     * Initializes flight routes from this airport to others.
     * 
     * @param airport The airport to add.
     */
    public void addAirport(Airport airport) {
        airports.put(airport.getAirportCode(), airport);
        flightMap.putIfAbsent(airport, new ArrayList<>());
    }

    /**
     * Adds an airfield to the airline's network.
     * 
     * @param airfield The airfield to add.
     */
    public void addAirfield(Airfield airfield) {
        airfields.put(airfield.getAirfieldName(), airfield);
    }

    /**
     * Retrieves an airfield by its name.
     * 
     * @param name The name of the airfield.
     * @return The airfield with the given name.
     */
    public Airfield getAirfield(String name) {
        return airfields.get(name);
    }

    /**
     * Retrieves an airport by its code.
     * 
     * @param code The code of the airport.
     * @return The airport with the given code.
     */
    public Airport getAirport(String code) {
        return airports.get(code);
    }

    // Getters for flight map and airports map
    public Map<Airport, List<Airport>> getFlightMap() {
        return flightMap;
    }
    public Map<String, Airport> getAirports() {
        return airports;
    }

    /**
     * Adds a flight direction from one airport to another in the airline's network.
     * 
     * @param from The origin airport.
     * @param to The destination airport.
     */
    public void addFlightDirection(Airport from, Airport to) {
        flightMap.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
    }

    /**
     * Finds the most cost-effective succession of flights from an origin to a destination airport.
     * The algorithm considers flight costs based on distance and weather conditions.
     * 
     * @param origin The starting airport.
     * @param destination The target airport.
     * @param time The time of travel for weather consideration.
     * @return A list of airports representing the flight path.
     */
    public List<Airport> findSuccessiveFlights(Airport origin, Airport destination, long time) {
        // Priority queue to select the next airport with the minimum cumulative cost
        PriorityQueue<AirportNode> pq = new PriorityQueue<>(Comparator.comparingDouble(AirportNode::getCost));

        // Map to store the best cost to reach each airport
        Map<Airport, Double> costToReach = new HashMap<>();

        // Map to reconstruct the path
        Map<Airport, Airport> previousAirport = new HashMap<>();

        // Initialize the cost to reach each airport as infinity
        for (Airport airport : flightMap.keySet()) {
            costToReach.put(airport, Double.MAX_VALUE);
        }

        // Start with the origin airport
        costToReach.put(origin, 0.0);
        pq.offer(new AirportNode(origin, 0.0));

        while (!pq.isEmpty()) {
            AirportNode current = pq.poll();
            Airport currentAirport = current.getAirport();

            // If we reach the destination, we can reconstruct and return the path
            if (currentAirport.equals(destination)) {
                return reconstructPath(previousAirport, destination);
            }

            // Relaxation step
            for (Airport neighbor : flightMap.getOrDefault(currentAirport, Collections.emptyList())) {
                double newCost = costToReach.get(currentAirport) + getFlightCost(currentAirport, neighbor,time);
                if (newCost < costToReach.get(neighbor)) {
                    costToReach.put(neighbor, newCost);
                    previousAirport.put(neighbor, currentAirport);
                    pq.offer(new AirportNode(neighbor, newCost));
                }
            }
        }

        // Return an empty list if no path is found
        return Collections.emptyList();
    }

    private List<Airport> reconstructPath(Map<Airport, Airport> previousAirport, Airport destination) {
        List<Airport> path = new ArrayList<>();
        for (Airport at = destination; at != null; at = previousAirport.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371; // Radius of the Earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // convert to kilometers
    
        return distance;
    }
    

    public double getFlightCost(Airport from, Airport to, long time) {
        double distance = calculateDistance(from.getLatitude(), from.getLongitude(), to.getLatitude(), to.getLongitude());
        Airfield airfield1 = airfields.get(from.getAirfieldName());
        Airfield airfield2 = airfields.get(to.getAirfieldName());
        double wd = airfield1.calculateWeatherMultiplier(time);
        double wl = airfield2.calculateWeatherMultiplier(time);
        double cost = 300 * wd  * wl + distance;
        return cost;
    }

    // Additional methods and logic...

    // Helper class to manage airports in the priority queue
    private static class AirportNode {
        private Airport airport;
        private double cost;

        public AirportNode(Airport airport, double cost) {
            this.airport = airport;
            this.cost = cost;
        }

        public Airport getAirport() {
            return airport;
        }

        public double getCost() {
            return cost;
        }
    }
      
    
}
