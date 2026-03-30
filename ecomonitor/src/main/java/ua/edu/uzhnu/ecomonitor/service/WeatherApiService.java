package ua.edu.uzhnu.ecomonitor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.edu.uzhnu.ecomonitor.model.Measurement;
import ua.edu.uzhnu.ecomonitor.repository.MeasurementRepository;

import java.util.List;
import java.util.Map;

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

    // Рефакторинг: Оптимізовано планувальник для щогодинного фонового збору (Cron: 0 0 * * * *)
    @Scheduled(cron = "0 0 * * * *")
    public Measurement fetchAndSaveData() {
        System.out.println("Scheduler execution started: Fetching fresh environmental metrics...");

        String airUrl = String.format(
                "http://api.openweathermap.org/data/2.5/air_pollution?lat=%s&lon=%s&appid=%s",
                lat, lon, apiKey
        );

        String weatherUrl = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric",
                lat, lon, apiKey
        );

        try {
            Map<String, Object> airResponse = restTemplate.getForObject(airUrl, Map.class);
            Map<String, Object> weatherResponse = restTemplate.getForObject(weatherUrl, Map.class);

            if (airResponse != null && weatherResponse != null) {
                Measurement measurement = new Measurement();

                // Оновлений парсинг екологічних маркерів
                List<Map<String, Object>> list = (List<Map<String, Object>>) airResponse.get("list");
                Map<String, Object> components = (Map<String, Object>) list.get(0).get("components");

                measurement.setPm25(((Number) components.get("pm2_5")).doubleValue());
                measurement.setPm10(((Number) components.get("pm10")).doubleValue());
                measurement.setNo2(((Number) components.get("no2")).doubleValue());

                // Оновлений парсинг метеорологічних маркерів
                Map<String, Object> main = (Map<String, Object>) weatherResponse.get("main");
                measurement.setTemperature(((Number) main.get("temp")).doubleValue());
                measurement.setHumidity(((Number) main.get("humidity")).doubleValue());

                System.out.println("Scheduler transaction pipeline completed successfully.");
                return repository.save(measurement);
            }
        } catch (Exception e) {
            System.err.println("CRITICAL: Failed to process automated data collection task: " + e.getMessage());
        }
        return null;
    }
}