package weather_data_api;

import org.springframework.web.bind.annotation.*;

@RestController
public class WeatherController {

    @GetMapping("/weather")
    public String getWeather(@RequestParam String location) {
        return "Weather for " + location;
    }
}