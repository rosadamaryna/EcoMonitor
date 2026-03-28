package ua.edu.uzhnu.ecomonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.edu.uzhnu.ecomonitor.model.Measurement;

@Controller
public class MainController {

    // Головна сторінка
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("latest", new Measurement());
        return "index";
    }
}