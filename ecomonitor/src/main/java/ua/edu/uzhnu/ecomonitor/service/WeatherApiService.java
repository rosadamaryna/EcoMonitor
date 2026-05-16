package ua.edu.uzhnu.ecomonitor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.edu.uzhnu.ecomonitor.model.Measurement;
import ua.edu.uzhnu.ecomonitor.repository.MeasurementRepository;

import java.time.LocalDateTime;
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

    // --- СЛОВНИК ОПОРНИХ ЕКОЛОГІЧНИХ СТАНЦІЙ В УЖГОРОДІ ---
    public static final Map<String, double[]> STATIONS = Map.of(
            "Центр", new double[]{48.6212, 22.2978},
            "БАМ (УжНУ)", new double[]{48.6341, 22.2882},
            "Шахта", new double[]{48.6385, 22.3122},
            "Радванка", new double[]{48.6110, 22.3280}
    );

    public WeatherApiService(MeasurementRepository repository) {
        this.repository = repository;
        this.restTemplate = new RestTemplate();
    }

    // Твій оригінальний метод (залишаємо без змін для сумісності з поточним планувальником)
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
                Measurement measurement = new Measurement();

                List<Map<String, Object>> list = (List<Map<String, Object>>) airResponse.get("list");
                Map<String, Object> components = (Map<String, Object>) list.get(0).get("components");

                measurement.setPm25(((Number) components.get("pm2_5")).doubleValue());
                measurement.setPm10(((Number) components.get("pm10")).doubleValue());
                measurement.setNo2(((Number) components.get("no2")).doubleValue());

                Map<String, Object> main = (Map<String, Object>) weatherResponse.get("main");
                measurement.setTemperature(((Number) main.get("temp")).doubleValue());
                measurement.setHumidity(((Number) main.get("humidity")).doubleValue());

                measurement.setTimestamp(LocalDateTime.now());

                return repository.save(measurement);
            }
        } catch (Exception e) {
            System.err.println("Помилка API: " + e.getMessage());
        }
        return null;
    }

    // --- НОВИЙ ПАРАМЕТРИЗОВАНИЙ МЕТОД ДЛЯ ПЕВНОЇ ЛОКАЦІЇ ---
    @SuppressWarnings("unchecked")
    public Measurement fetchAndSaveDataForLocation(String locationName, double customLat, double customLon) {
        String airUrl = String.format(
                "http://api.openweathermap.org/data/2.5/air_pollution?lat=%s&lon=%s&appid=%s",
                customLat, customLon, apiKey
        );

        String weatherUrl = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric",
                customLat, customLon, apiKey
        );

        try {
            Map<String, Object> airResponse = restTemplate.getForObject(airUrl, Map.class);
            Map<String, Object> weatherResponse = restTemplate.getForObject(weatherUrl, Map.class);

            if (airResponse != null && weatherResponse != null) {
                Measurement measurement = new Measurement();

                // Зберігаємо гео-маркери
                measurement.setLocationName(locationName);
                measurement.setLatitude(customLat);
                measurement.setLongitude(customLon);

                List<Map<String, Object>> list = (List<Map<String, Object>>) airResponse.get("list");
                Map<String, Object> components = (Map<String, Object>) list.get(0).get("components");

                measurement.setPm25(((Number) components.get("pm2_5")).doubleValue());
                measurement.setPm10(((Number) components.get("pm10")).doubleValue());
                measurement.setNo2(((Number) components.get("no2")).doubleValue());

                Map<String, Object> main = (Map<String, Object>) weatherResponse.get("main");
                measurement.setTemperature(((Number) main.get("temp")).doubleValue());
                measurement.setHumidity(((Number) main.get("humidity")).doubleValue());

                measurement.setTimestamp(LocalDateTime.now());

                return repository.save(measurement);
            }
        } catch (Exception e) {
            System.err.println("Помилка API для локації " + locationName + ": " + e.getMessage());
        }
        return null;
    }
}