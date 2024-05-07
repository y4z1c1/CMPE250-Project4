/**
 * Represents an airport with its unique attributes.
 * This class encapsulates details such as airport code, airfield name, geographic coordinates, and parking cost.
 * It provides methods to access and modify these attributes.
 * @author Yusuf Anil Yazici
 */
public class Airport {
    // Unique identifier for the airport
    private String airportCode;
    
    // Name of the airfield associated with the airport
    private String airfieldName;
    
    // Geographic latitude of the airport
    private double latitude;
    
    // Geographic longitude of the airport
    private double longitude;
    
    // Cost for parking at the airport
    private double parkingCost;

    /**
     * Constructs a new Airport instance with specified details.
     * 
     * @param airportCode Unique airport code.
     * @param airfieldName Name of the airfield.
     * @param latitude Geographic latitude.
     * @param longitude Geographic longitude.
     * @param parkingCost Cost of parking at the airport.
     */
    public Airport(String airportCode, String airfieldName, double latitude, double longitude, double parkingCost) {
        this.airportCode = airportCode;
        this.airfieldName = airfieldName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.parkingCost = parkingCost;
    }

    // Getter method for airport code
    public String getAirportCode() {
        return airportCode;
    }

    // Setter method for airport code
    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    // Getter method for airfield name
    public String getAirfieldName() {
        return airfieldName;
    }

    // Setter method for airfield name
    public void setAirfieldName(String airfieldName) {
        this.airfieldName = airfieldName;
    }

    // Getter method for latitude
    public double getLatitude() {
        return latitude;
    }

    // Setter method for latitude
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    // Getter method for longitude
    public double getLongitude() {
        return longitude;
    }

    // Setter method for longitude
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Getter method for parking cost
    public double getParkingCost() {
        return parkingCost;
    }

    // Setter method for parking cost
    public void setParkingCost(double parkingCost) {
        this.parkingCost = parkingCost;
    }

}
