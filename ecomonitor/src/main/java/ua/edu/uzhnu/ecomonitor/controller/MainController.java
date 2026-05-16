package ua.edu.uzhnu.ecomonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String index(Model model) {
        List<Measurement> history;
        Measurement latest;

        try {
            history = measurementService.getLatestMeasurements();
            // Якщо в базі немає записів, створюємо новий об'єкт, щоб уникнути NullPointerException в Thymeleaf
            latest = (history == null || history.isEmpty()) ? new Measurement() : history.get(0);
        } catch (Exception e) {
            // Якщо база даних недоступна, перехоплюємо помилку та повертаємо безпечні дефолтні значення
            history = Collections.emptyList();
            latest = new Measurement();
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
            // У разі падіння бази даних повертаємо порожній список, щоб сторінка історії завантажилася без помилки 500
            allHistory = Collections.emptyList();
        }

        model.addAttribute("fullHistory", allHistory);
        return "history";
    }
}