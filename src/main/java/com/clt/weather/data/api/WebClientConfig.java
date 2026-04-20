package com.clt.weather.data.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration

public class WebClientConfig {

	@Bean
	public WebClient weatherWebclient() 
	{
		return WebClient.builder().baseUrl("https://api.open-meteo.com").build();
		
	}
	@Bean
	public WebClient geoWebclient() 
	{
		return WebClient.builder().baseUrl("https://geocoding-api.open-meteo.com").build();
		
	}

}

