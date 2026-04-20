package com.clt.weather.data.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration

public class WebClientConfig {

	@Bean
	public WebClient weatherWebclient(WebClient.Builder builder) 
	{
		return builder.baseUrl("https://api.open-meteo.com").build();
		
	}
	@Bean
	public WebClient geoWebclient(WebClient.Builder builder) 
	{
		return builder.baseUrl("https://geocoding-api.open-meteo.com").build();
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
