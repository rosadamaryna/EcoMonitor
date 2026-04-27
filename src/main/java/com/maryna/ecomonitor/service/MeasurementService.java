package com.maryna.ecomonitor.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maryna.ecomonitor.model.Measurement;
import com.maryna.ecomonitor.repository.MeasurementRepository;

@Service
public class MeasurementService {

    @Autowired
    private MeasurementRepository repository;

    @Autowired
    private WeatherApiService weatherApiService;

    @SuppressWarnings("unchecked") // Прибирає жовті попередження про Type safety
    public void fetchAndSaveData(String city, double lat, double lon) {
        try {
            Map<String, Object> airData = weatherApiService.getAirPollutionData(lat, lon);
            Map<String, Object> weatherData = weatherApiService.getWeatherData(city);

            Measurement measurement = new Measurement();

            // Парсинг Air Pollution
            List<Map<String, Object>> list = (List<Map<String, Object>>) airData.get("list");
            Map<String, Object> mainAir = list.get(0);
            Map<String, Object> components = (Map<String, Object>) mainAir.get("components");

            measurement.setPm25(((Number) components.get("pm2_5")).doubleValue());
            measurement.setPm10(((Number) components.get("pm10")).doubleValue());
            measurement.setNo2(((Number) components.get("no2")).doubleValue());

            // Парсинг Weather
            Map<String, Object> mainWeather = (Map<String, Object>) weatherData.get("main");
            measurement.setTemp(((Number) mainWeather.get("temp")).doubleValue());
            measurement.setHumidity(((Number) mainWeather.get("humidity")).doubleValue());

            measurement.setTimestamp(LocalDateTime.now());

            repository.save(measurement);
            System.out.println("✅ Дані збережено для міста: " + city);

        } catch (Exception e) {
            System.err.println("❌ Помилка: " + e.getMessage());
            e.printStackTrace(); 
        }
    }
}