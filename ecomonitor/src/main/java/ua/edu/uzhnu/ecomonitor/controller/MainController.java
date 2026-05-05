package ua.edu.uzhnu.ecomonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.edu.uzhnu.ecomonitor.model.Measurement;
import ua.edu.uzhnu.ecomonitor.service.MeasurementService;
import java.util.List;

@Controller
public class MainController {

    private final MeasurementService measurementService;

    public MainController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    // Головна сторінка
    @GetMapping("/")
    public String index(Model model) {
        List<Measurement> history = measurementService.getLatestMeasurements();
        Measurement latest = history.isEmpty() ? null : history.get(0);

        model.addAttribute("latest", latest);
        model.addAttribute("history", history);
        return "index";
    }

    // Сторінка історії
    @GetMapping("/history")
    public String history(Model model) {
        List<Measurement> allHistory = measurementService.getAllMeasurements();
        model.addAttribute("fullHistory", allHistory);
        return "history";
    }
}