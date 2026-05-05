package ua.edu.uzhnu.ecomonitor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "measurements")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double pm25;
    private Double pm10;
    private Double no2;
    private Double temperature;
    private Double humidity;
    private LocalDateTime timestamp;

    public Measurement() {}

    // Геттери та сеттери
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getPm25() { return pm25; }
    public void setPm25(Double pm25) { this.pm25 = pm25; }
    public Double getPm10() { return pm10; }
    public void setPm10(Double pm10) { this.pm10 = pm10; }
    public Double getNo2() { return no2; }
    public void setNo2(Double no2) { this.no2 = no2; }
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    public Double getHumidity() { return humidity; }
    public void setHumidity(Double humidity) { this.humidity = humidity; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    // --- Методи для обробки та класифікації екологічних показників ---

    public String getAqiStatus() {
        if (pm25 == null) return "Немає даних";
        if (pm25 <= 12.0) return "Відмінно";
        if (pm25 <= 35.4) return "Задовільно";
        if (pm25 <= 55.4) return "Шкідливо";
        return "Небезпечно";
    }

    public String getOverallStatus() {
        // Перевірка на null, щоб уникнути помилки 500
        if (pm25 == null || pm10 == null || no2 == null) return "Оновлення...";

        // Логіка "найгіршого показника"
        if (pm25 > 55.4 || pm10 > 150 || no2 > 100) return "Небезпечно";
        if (pm25 > 35.4 || pm10 > 100 || no2 > 80) return "Шкідливо";
        if (pm25 > 12.0 || pm10 > 50 || no2 > 40) return "Задовільно";

        return "Відмінно";
    }

    // --- Методи динамічного підбору кольорів для веб-інтерфейсу (HEX-коди) ---

    public String getAqiColor() {
        if (pm25 == null) return "#808080";
        if (pm25 <= 12.0) return "#00e400";
        if (pm25 <= 35.4) return "#ffff00";
        if (pm25 <= 55.4) return "#ff7e00";
        return "#ff0000";
    }

    public String getPm10Color() {
        if (pm10 == null) return "#64748b";
        return (pm10 <= 50) ? "#10b981" : (pm10 <= 100) ? "#f59e0b" : "#ef4444";
    }

    public String getNo2Color() {
        if (no2 == null) return "#64748b";
        return (no2 <= 40) ? "#10b981" : (no2 <= 80) ? "#f59e0b" : "#ef4444";
    }

    public String getTempColor() {
        if (temperature == null) return "#64748b";
        if (temperature > 30 || temperature < 0) return "#ef4444";
        return (temperature >= 15 && temperature <= 25) ? "#10b981" : "#3b82f6";
    }
}