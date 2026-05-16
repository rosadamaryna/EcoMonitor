package ua.edu.uzhnu.ecomonitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.edu.uzhnu.ecomonitor.model.Measurement;
import ua.edu.uzhnu.ecomonitor.repository.MeasurementRepository;
import java.util.List;
import java.util.Map;

@Service
public class MeasurementService {

    @Autowired
    private WeatherApiService weatherApiService;

    @Autowired
    private MeasurementRepository repository;

    // ОНОВЛЕНИЙ ПЛАНУВАЛЬНИК: обходить по черзі всі 4 станції міста
    @Scheduled(fixedRate = 600000, initialDelay = 5000)
    public void updateAndSaveData() {
        for (Map.Entry<String, double[]> entry : WeatherApiService.STATIONS.entrySet()) {
            String name = entry.getKey();
            double lat = entry.getValue()[0];
            double lon = entry.getValue()[1];
            weatherApiService.fetchAndSaveDataForLocation(name, lat, lon);
        }
    }

    // НОВИЙ МЕТОД: Обчислення найближчої екологічної станції до користувача
    public String findNearestStation(Double userLat, Double userLng) {
        if (userLat == null || userLng == null) {
            return "Центр"; // Якщо геолокацію в браузері вимкнено — повертаємо дефолт
        }

        String nearestStation = "Центр";
        double minDistance = Double.MAX_VALUE;

        // Шукаємо мінімальну геометричну відстань до опорних точок
        for (Map.Entry<String, double[]> entry : WeatherApiService.STATIONS.entrySet()) {
            double stationLat = entry.getValue()[0];
            double stationLng = entry.getValue()[1];

            double deltaLat = userLat - stationLat;
            double deltaLng = userLng - stationLng;
            double distance = Math.sqrt(deltaLat * deltaLat + deltaLng * deltaLng);

            if (distance < minDistance) {
                minDistance = distance;
                nearestStation = entry.getKey();
            }
        }
        return nearestStation;
    }

    // Отримання вимірів для графіків конкретної локації
    public List<Measurement> getLatestMeasurementsForLocation(String locationName) {
        return repository.findTop20ByLocationNameOrderByTimestampDesc(locationName);
    }

    public List<Measurement> getLatestMeasurements() {
        return repository.findTop20ByOrderByTimestampDesc();
    }
    public List<Measurement> getAllMeasurements() {
        return repository.findAllByOrderByTimestampDesc();
    }
}