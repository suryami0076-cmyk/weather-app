package com.clt.weather.data.api;

import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WeatherService {

    private final WebClient weatherWebClient;
    private final WebClient geoWebClient;

    public WeatherService(
            @Qualifier("weatherWebclient") WebClient weatherWebClient,
            @Qualifier("geoWebclient") WebClient geoWebClient) {

        this.weatherWebClient = weatherWebClient;
        this.geoWebClient = geoWebClient;
    }

    public GeoCodingResponseDTO.Location getCoordinates(int location) {

        GeoCodingResponseDTO response = geoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search")
                        .queryParam("name", location)
                        .queryParam("count", 1)
                        .queryParam("language", "en")
                        .build())
                .retrieve()
                .bodyToMono(GeoCodingResponseDTO.class)
                .block();

        return (response != null
                && response.getResults() != null
                && !response.getResults().isEmpty())
                ? response.getResults().get(0)
                : null;
    }

    public WeatherResponseDTO getWeather(double latitude, double longitude) {

        WeatherApiResponseDTO response = weatherWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("current_weather", true)
                        .build())
                .retrieve()
                .bodyToMono(WeatherApiResponseDTO.class)
                .block();

        WeatherResponseDTO weatherResponse = response != null
                ? response.getCurrentWeather()
                : null;

        if (weatherResponse != null) {
            weatherResponse.setWeatherDescription(
                    getWeatherDescription(weatherResponse.getWeathercode())
            );
        }

        return weatherResponse;
    }

 
    // step1 : convert location nmae to lat/longitude
    
    public GeoCodingResponseDTO.Location getCoordinates(String location) {

        GeoCodingResponseDTO response = geoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search")
                        .queryParam("name", location)
                        .queryParam("count", 1)
                        .queryParam("language", "en")
                        .build())
                .retrieve()
                .bodyToMono(GeoCodingResponseDTO.class)
                .block();

        return (response !=null && !response.getResults().isEmpty()) ? response.getResults().get(0) : null;
                
    }
 
    // step2 get weather data using lat and logitude
    
    public WeatherResponseDTO getWeather(String location) {
    	
    	GeoCodingResponseDTO.Location coordinates = getCoordinates (location);
    	
    	if(coordinates == null) 
    	{
    		
    		return null;
    	}
    		
 //  Map JSON to weatheraApiResponse
        WeatherApiResponseDTO response =weatherWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast")
                        .queryParam("latitude", coordinates.getLatitude())
                        .queryParam("longitude", coordinates.getLongitude())
                        .queryParam("current_weather", "true")
                        .build())
                .retrieve()
                .bodyToMono(WeatherApiResponseDTO.class)
                .block();

        WeatherResponseDTO weatherResponse = response.getCurrentWeather();
        
        // add weather description and Emoji
        weatherResponse.setWeatherDescription(getWeatherDescription(weatherResponse.getWeathercode()));
        weatherResponse.setWeatherIcon(getWeatherIcon(weatherResponse.getWeathercode()));
        
        return weatherResponse;
    }
    // map weather code to description
    private String getWeatherDescription(int code) 
    {
    Map<Integer , String>weatherDescriptions = Map.ofEntries(
    	 Map.entry(0, "Clear sky"),
    	    Map.entry(1, "Mainly clear"),
    	    Map.entry(2, "Partly cloudy"),
    	    Map.entry(3, "Overcast"),
    	    Map.entry(45, "Fog"),
    	    Map.entry(48, "Depositing rime fog"),
    	    Map.entry(51, "Light drizzle"),
    	    Map.entry(53, "Moderate drizzle"),
    	    Map.entry(55, "Heavy drizzle"),
    	    Map.entry(56, "Freezing drizzle"),
    	    Map.entry(57, "Heavy freezing drizzle"),
    	    Map.entry(61, "Light rain"),
    	    Map.entry(63, "Moderate rain"),
    	    Map.entry(65, "Heavy rain"),
    	    Map.entry(80, "Rain showers"),
    	    Map.entry(81, "Heavy rain showers"),
    	    Map.entry(82, "Violent rain showers"),
    	    Map.entry(95, "Thunderstorm"),
    	    Map.entry(96, "Thunderstorm with hail"),
    	    Map.entry(99, "Severe thunderstorm with hail")
    	    );
    
        return weatherDescriptions .getOrDefault(code, "Unknown Weather");
    }
    
    //map weather code  to Emoji icon
    
    private String getWeatherIcon (int code) {
    	
    Map<Integer , String>weatherIcons = Map.ofEntries(
    Map.entry(0, "☀️"),  // Clear sky
    Map.entry(1, "🌤️"),  // Mainly clear
    Map.entry(2, "⛅"),   // Partly cloudy
    Map.entry(3, "☁️"),  // Overcast
    Map.entry(45, "🌫️"), // Fog
    Map.entry(48, "🌁"),  // Depositing rime fog	
    Map.entry(51, "🌦️"), // Light drizzle
    Map.entry(53, "🌧️"), // Moderate drizzle
    Map.entry(55, "🌧️"), // Heavy drizzle
    Map.entry(56, "🌨️"), // Freezing drizzle
    Map.entry(57, "❄️"),  // Heavy freezing drizzle
    Map.entry(61, "🌧️"), // Light rain
    Map.entry(63, "🌧️"), // Moderate rain
    Map.entry(65, "🌧️"), // Heavy rain
    Map.entry(80, "🌦️"), // Rain showers
    Map.entry(81, "🌧️"), // Heavy rain showers
    Map.entry(82, "⛈️"), // Violent rain showers
    Map.entry(95, "🌩️"), // Thunderstorm
    Map.entry(96, "⛈️"), // Thunderstorm with hail
    Map.entry(99, "⛈️") // Severe thunderstorm with hail
   );
    
    return weatherIcons.getOrDefault(code, "❓"); // Default icon if code not found
    
    }  
}