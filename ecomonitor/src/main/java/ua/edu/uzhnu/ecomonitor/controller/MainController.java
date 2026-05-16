package ua.edu.uzhnu.ecomonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.edu.uzhnu.ecomonitor.model.Measurement;
import ua.edu.uzhnu.ecomonitor.service.MeasurementService;

import java.util.Collections;
import java.util.List;

@Controller
public class MainController {

    private final MeasurementService measurementService;

    public MainController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping("/")
    public String index(
            @RequestParam(value = "lat", required = false) Double lat,
            @RequestParam(value = "lng", required = false) Double lng,
            Model model) {

        List<Measurement> history;
        Measurement latest;
        String currentStation = "Центр";

        try {
            // 1. Визначаємо найближчу станцію за координатами з браузера
            currentStation = measurementService.findNearestStation(lat, lng);

            // 2. Пробуємо завантажити дані для цієї конкретної станції
            history = measurementService.getLatestMeasurementsForLocation(currentStation);

            if (history == null || history.isEmpty()) {
                // Якщо для нової локації ще немає записів у базі,
                // беремо будь-який останній наявний запис із загальної таблиці, щоб екран не був порожнім.
                List<Measurement> generalHistory = measurementService.getAllMeasurements();
                if (generalHistory != null && !generalHistory.isEmpty()) {
                    latest = generalHistory.get(0);
                    history = generalHistory;
                } else {
                    latest = new Measurement(); // Повністю дефолтний об'єкт, якщо база чиста
                }
            } else {
                latest = history.get(0);
            }

            // Примусово вписуємо назву визначеної локації, щоб вона відобразилась у шапці
            latest.setLocationName(currentStation);

        } catch (Exception e) {
            history = Collections.emptyList();
            latest = new Measurement();
            latest.setLocationName(currentStation);
        }

        model.addAttribute("latest", latest);
        model.addAttribute("history", history);
        return "index";
    }

    @GetMapping("/history")
    public String history(Model model) {
        List<Measurement> allHistory;

        try {
            allHistory = measurementService.getAllMeasurements();
            if (allHistory == null) {
                allHistory = Collections.emptyList();
            }
        } catch (Exception e) {
            allHistory = Collections.emptyList();
        }

        model.addAttribute("fullHistory", allHistory);
        return "history";
    }
}