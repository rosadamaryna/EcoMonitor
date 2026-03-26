package ua.edu.uzhnu.ecomonitor.service;

import org.springframework.beans.factory.annotation.Value;
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

    public Measurement fetchAndSaveData() {
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
                // Парсинг екології
                List<Map<String, Object>> list = (List<Map<String, Object>>) airResponse.get("list");
                Map<String, Object> components = (Map<String, Object>) list.get(0).get("components");

                Double pm25 = ((Number) components.get("pm2_5")).doubleValue();
                Double pm10 = ((Number) components.get("pm10")).doubleValue();
                Double no2 = ((Number) components.get("no2")).doubleValue();

                // Парсинг погоди
                Map<String, Object> main = (Map<String, Object>) weatherResponse.get("main");
                Double temperature = ((Number) main.get("temp")).doubleValue();
                Double humidity = ((Number) main.get("humidity")).doubleValue();

                System.out.println(String.format(
                        "API Data Fetched successfully! PM2.5: %.2f, Temp: %.2f°C", pm25, temperature
                ));
            }
        } catch (Exception e) {
            System.err.println("Помилка при отриманні даних з API: " + e.getMessage());
        }

        // Поки що повертаємо null
        return null;
    }
}