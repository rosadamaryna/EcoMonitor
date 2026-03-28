package ua.edu.uzhnu.ecomonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcomonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcomonitorApplication.class, args);
    }
}