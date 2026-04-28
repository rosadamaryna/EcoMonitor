package ua.edu.uzhnu.ecomonitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.edu.uzhnu.ecomonitor.model.Measurement;
import ua.edu.uzhnu.ecomonitor.repository.MeasurementRepository;
import java.util.List;

@Service
public class MeasurementService {
    @Autowired
    private WeatherApiService weatherApiService;
    @Autowired
    private MeasurementRepository repository;

    @Scheduled(fixedRate = 600000, initialDelay = 5000)
    public void updateAndSaveData() {
        // Просто викликаємо оновлення, нікуди не присвоюємо результат
        weatherApiService.fetchAndSaveData();
    }

    public List<Measurement> getLatestMeasurements() {
        return repository.findTop20ByOrderByTimestampDesc();
    }
    public List<Measurement> getAllMeasurements() {
        // Повертаємо всі записи, відсортовані від нових до старих
        return repository.findAllByOrderByTimestampDesc();
    }
}