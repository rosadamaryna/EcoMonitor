package com.maryna.ecomonitor.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "measurements")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Показники якості повітря
    private Double pm25;
    private Double pm10;
    private Double no2;

    // Метеорологічні дані
    private Double temp;
    private Double humidity;

    // Час проведення заміру
    private LocalDateTime timestamp;

    // Конструктор без параметрів (обов'язковий для JPA)
    public Measurement() {
    }

    // Геттери та Сеттери
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getPm25() { return pm25; }
    public void setPm25(Double pm25) { this.pm25 = pm25; }

    public Double getPm10() { return pm10; }
    public void setPm10(Double pm10) { this.pm10 = pm10; }

    public Double getNo2() { return no2; }
    public void setNo2(Double no2) { this.no2 = no2; }

    public Double getTemp() { return temp; }
    public void setTemp(Double temp) { this.temp = temp; }

    public Double getHumidity() { return humidity; }
    public void setHumidity(Double humidity) { this.humidity = humidity; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}