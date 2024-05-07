/**
 * Represents an airfield with the capability to manage weather conditions.
 * This class stores weather data for different times and provides methods to add new weather conditions,
 * retrieve existing ones, and calculate a weather multiplier based on weather codes.
* @author Yusuf Anil Yazici

 */

import java.util.HashMap;
import java.util.Map;

public class Airfield {
    // Unique identifier for the airfield
    private String airfieldName;

    // Map to hold weather conditions at different times
    private Map<Long, Integer> weatherConditions;

    // Constructor
    public Airfield(String airfieldName) {
        this.airfieldName = airfieldName;
        this.weatherConditions = new HashMap<>();
    }

    // Method to add weather condition for a specific time
    public void addWeatherCondition(long time, int weatherCode) {
        weatherConditions.put(time, weatherCode);
    }

    // Get the weather condition for a specific time
    // Returns null if there is no weather data for the given time
    public Integer getWeatherCondition(long time) {
        return weatherConditions.get(time);
    }

    // Getters and setters
    public String getAirfieldName() {
        return airfieldName;
    }

    public void setAirfieldName(String airfieldName) {
        this.airfieldName = airfieldName;
    }

    public Map<Long, Integer> getWeatherConditions() {
        return weatherConditions;
    }

    // Method to calculate and return the weather multiplier for a given time
    public double calculateWeatherMultiplier(long time) {
        Integer weatherCode = getWeatherCondition(time);
        
        // Check if there is a weather condition for the given time
        if (weatherCode == null) {
            // No weather data available for the given time, return default multiplier
            return 1.0;
        }

        // Extract weather conditions from the binary representation of the weather code
        boolean Bw = (weatherCode & 16) != 0; // Wind
        boolean Br = (weatherCode & 8) != 0;  // Rain
        boolean Bs = (weatherCode & 4) != 0;  // Snow
        boolean Bh = (weatherCode & 2) != 0;  // Hail
        boolean Bb = (weatherCode & 1) != 0;  // Bolt

        // Calculate the weather multiplier
        double W = (Bw ? 1.05 : 1.0) * (Br ? 1.05 : 1.0) * (Bs ? 1.10 : 1.0) *
                   (Bh ? 1.15 : 1.0) * (Bb ? 1.20 : 1.0);

        return W;
    }
}
