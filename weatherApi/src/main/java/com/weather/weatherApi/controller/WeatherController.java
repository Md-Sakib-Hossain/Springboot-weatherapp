package com.weather.weatherApi.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.weather.weatherApi.model.WeatherDetails;
import com.weather.weatherApi.model.WeatherResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;


@CrossOrigin(origins="http://localhost:4200")
@RestController
public class WeatherController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/weather")
    public Object getWeather(@RequestParam("city") String city) {
        // Step 1: Get latitude and longitude using OpenStreetMap Nominatim API
        WeatherResponse[] locationResponse = fetchLocation(city);

        if (locationResponse != null && locationResponse.length > 0) {
            // Get latitude and longitude from the first response
            double lat = Double.parseDouble(locationResponse[0].getLat());
            double lon = Double.parseDouble(locationResponse[0].getLon());

            // Step 2: Use latitude and longitude to get weather details from Open-Meteo API
            WeatherDetails weatherDetails = fetchWeatherDetails(lat, lon);

            if (weatherDetails != null && weatherDetails.getHourly() != null) {
                // Step 3: Classify weather based on temperature
                String[] weatherConditions = classifyWeatherConditions(weatherDetails.getHourly().getTemperature_2m());
                weatherDetails.getHourly().setWeatherCondition(weatherConditions);

                // Step 4: Combine data into a new format
                return formatWeatherData(weatherDetails);
            }
        }
        return null;  // Return null if no data found (or handle error response)
    }

    // Method to fetch location details using OpenStreetMap Nominatim API
    private WeatherResponse[] fetchLocation(String city) {
        String locationUrl = "https://nominatim.openstreetmap.org/search?format=json&q=" + city;
        return restTemplate.getForObject(locationUrl, WeatherResponse[].class);
    }

    // Method to fetch weather details using Open-Meteo API
    private WeatherDetails fetchWeatherDetails(double lat, double lon) {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        // Generate the weather URL with the dynamic end date
        String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon
                + "&hourly=temperature_2m,wind_speed_10m,relative_humidity_2m,pressure_msl&start_date=2025-02-20"
                + "&end_date=" + currentDate + "&timezone=auto";
        return restTemplate.getForObject(weatherUrl, WeatherDetails.class);
    }

    // Method to classify weather based on temperature
    private String[] classifyWeatherConditions(double[] temperatures) {
        String[] conditions = new String[temperatures.length];

        for (int i = 0; i < temperatures.length; i++) {
            if (temperatures[i] < 10) {
                conditions[i] = "Cold ❄️";
            } else if (temperatures[i] >= 10 && temperatures[i] < 25) {
                conditions[i] = "Cool 🧣";
            } else if (temperatures[i] >= 25 && temperatures[i] < 29) {
                conditions[i] = "Warm 🌞";
            } else {
                conditions[i] = "Hot 🔥";
            }
        }

        return conditions;
    }

    // Method to format weather data into the desired format
    private Object[] formatWeatherData(WeatherDetails weatherDetails) {
        final String[] timeArray = weatherDetails.getHourly().getTime();
        final double[] tempArray = weatherDetails.getHourly().getTemperature_2m();
        final double[] windArray = weatherDetails.getHourly().getWind_speed_10m(); 
        final double[] humidityArray = weatherDetails.getHourly().getRelative_humidity_2m();
        final double[] pressureArray = weatherDetails.getHourly().getPressure_msl();
        final String[] weatherConditionsArray = weatherDetails.getHourly().getWeatherCondition();

        Object[] weatherSummary = new Object[timeArray.length];

        for (int i = 0; i < timeArray.length; i++) {
            // Declare a final variable for 'i'
            final int finalI = i;

            weatherSummary[finalI] = new Object() {
                public final String time = timeArray[finalI];
                public final double temperature = tempArray[finalI];
                public final double wind_speed = windArray[finalI];
                public final double humidity = humidityArray[finalI];
                public final double pressure = pressureArray[finalI];
                public final String weather_condition = weatherConditionsArray[finalI];
            };
        }

        return weatherSummary;  // Return the weather data in the desired format
    }
}
