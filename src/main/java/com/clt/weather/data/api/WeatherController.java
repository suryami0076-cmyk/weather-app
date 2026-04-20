package com.clt.weather.data.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public WeatherResponseDTO getWeather(@RequestParam String location)
            
        {

        return weatherService.getWeather(location);
    }

    @GetMapping("/")
    public String home() {
        return "Weather API is running successfully!";
    }
}