package ua.edu.uzhnu.ecomonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.uzhnu.ecomonitor.service.ChartService;

@RestController
public class ChartController {

    @Autowired
    private ChartService chartService;

    // Додано @RequestParam для приймання назви локації (наприклад: /chart/pm25?loc=Шахта)
    @GetMapping(value = "/chart/{type}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getChart(
            @PathVariable String type,
            @RequestParam(value = "loc", required = false, defaultValue = "Центр") String locationName) {
        try {
            // Передаємо два параметри: тип речовини та назву мікрорайону
            byte[] chartBytes = chartService.createChartBytes(type, locationName);
            if (chartBytes == null) {
                return new byte[0]; // Повертаємо порожній масив, якщо графік порожній
            }
            return chartBytes;
        } catch (Exception e) {
            // Якщо база лежить або сталася помилка, повертаємо порожній байтовий масив, щоб не ламати сторінку помилкою 500
            return new byte[0];
        }
    }
}