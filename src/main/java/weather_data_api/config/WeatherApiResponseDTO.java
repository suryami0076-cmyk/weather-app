package com.clt.weather.data.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherApiResponseDTO {

    @JsonProperty("current_weather")
    private WeatherResponseDTO currentWeather;

    public WeatherResponseDTO getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(WeatherResponseDTO currentWeather) {
        this.currentWeather = currentWeather;
    }
}