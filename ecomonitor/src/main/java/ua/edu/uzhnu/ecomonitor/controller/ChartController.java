package ua.edu.uzhnu.ecomonitor.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.uzhnu.ecomonitor.service.ChartService;

import java.io.IOException;

@RestController
public class ChartController {

    private final ChartService chartService;

    public ChartController(ChartService chartService) {
        this.chartService = chartService;
    }

    // Цей метод каже Spring: "Коли хтось просить /chart/..., виконай це"
    @GetMapping(value = "/chart/{type}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getChart(@PathVariable String type) throws IOException {
        byte[] chartBytes = chartService.createChartBytes(type);

        if (chartBytes == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(chartBytes);
    }
}