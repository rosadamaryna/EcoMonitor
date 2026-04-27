package com.maryna.ecomonitor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.maryna.ecomonitor.service.MeasurementService;

@SpringBootApplication
public class EcoMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcoMonitorApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(MeasurementService measurementService) {
        return args -> {
            System.out.println("🚀 Початок автоматичного збору даних...");
            
            // Координати Ужгорода (широта: 48.62, довгота: 22.30)
            measurementService.fetchAndSaveData("Uzhhorod", 48.62, 22.30);
            
            System.out.println("🏁 Процес завершено. Перевір таблицю в pgAdmin або консолі!");
        };
    }
}