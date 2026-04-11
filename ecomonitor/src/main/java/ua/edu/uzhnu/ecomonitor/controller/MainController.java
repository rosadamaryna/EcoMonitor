package ua.edu.uzhnu.ecomonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.edu.uzhnu.ecomonitor.model.Measurement;
import ua.edu.uzhnu.ecomonitor.repository.MeasurementRepository;

import java.util.List;

@Controller
public class MainController {

    private final MeasurementRepository measurementRepository;

    // Впровадження репозиторію через конструктор для отримання екологічних даних
    public MainController(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    /**
     * Маппінг для головної сторінки екологічного дашборду.
     * Метод завантажує останні вимірювання, додає їх до моделі
     * та повертає назву Thymeleaf-шаблону "index".
     */
    @GetMapping("/")
    public String showDashboard(Model model) {
        System.out.println("MainController: Processing request for the main dashboard...");

        List<Measurement> allMeasurements = measurementRepository.findAll();
        Measurement latestMeasurement = null;

        // Перевіряємо, чи є взагалі дані в базі даних, щоб уникнути помилок
        if (!allMeasurements.isEmpty()) {
            latestMeasurement = allMeasurements.get(allMeasurements.size() - 1);
        } else {
            // Створюємо порожній об'єкт із нульовими показниками, якщо база ще пуста
            latestMeasurement = new Measurement();
            System.out.println("MainController: Database is currently empty. Providing fallback empty model.");
        }

        // Передаємо дані у вебінтерфейс через модель Thymeleaf
        model.addAttribute("latest", latestMeasurement);
        model.addAttribute("history", allMeasurements);

        // Повертає назву файлу index.html
        return "index";
    }
}