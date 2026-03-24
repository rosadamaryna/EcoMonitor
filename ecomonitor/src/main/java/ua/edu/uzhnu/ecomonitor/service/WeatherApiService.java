package ua.edu.uzhnu.ecomonitor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.edu.uzhnu.ecomonitor.model.Measurement;
import ua.edu.uzhnu.ecomonitor.repository.MeasurementRepository;

@Service
public class WeatherApiService {

    private final MeasurementRepository repository;
    private final RestTemplate restTemplate;

    @Value("${api.openweathermap.key}")
    private String apiKey;

    @Value("${api.location.lat}")
    private String lat;

    @Value("${api.location.lon}")
    private String lon;

    public WeatherApiService(MeasurementRepository repository) {
        this.repository = repository;
        this.restTemplate = new RestTemplate();
    }

    public Measurement fetchAndSaveData() {
        // Заглушка: логіку запитів та парсингу JSON додамо у наступному коміті
        System.out.println("WeatherApiService: Ready to fetch data with key: " + apiKey);
        return null;
    }
}