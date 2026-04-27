package com.maryna.ecomonitor.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherApiService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    // Метод для отримання даних про якість повітря (Air Pollution API)
    public Map<String, Object> getAirPollutionData(double lat, double lon) {
        String url = String.format(
            "http://api.openweathermap.org/data/2.5/air_pollution?lat=%f&lon=%f&appid=%s",
            lat, lon, apiKey
        );
        
        return restTemplate.getForObject(url, Map.class);
    }

    // Метод для отримання погоди (Weather API)
    public Map<String, Object> getWeatherData(String city) {
        String url = String.format(
            "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric",
            city, apiKey
        );
        
        return restTemplate.getForObject(url, Map.class);
    }
}