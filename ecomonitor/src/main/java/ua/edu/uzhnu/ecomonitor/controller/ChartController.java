package ua.edu.uzhnu.ecomonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.uzhnu.ecomonitor.service.ChartService;

@RestController
public class ChartController {

    @Autowired
    private ChartService chartService;

    @GetMapping(value = "/chart/{type}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getChart(@PathVariable String type) {
        try {
            byte[] chartBytes = chartService.createChartBytes(type);
            if (chartBytes == null) {
                return new byte[0]; // Повертаємо порожній масив, якщо графік порожній
            }
            return chartBytes;
        } catch (Exception e) {
            // Якщо база лежить, повертаємо порожній байтовий масив, щоб картинка не ламала сторінку помилкою 500
            return new byte[0];
        }
    }
}