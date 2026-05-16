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

    // Текстовий статус для PM2.5
    public String getAqiStatus() {
        if (pm25 == null) return "Дані синхронізуються...";
        if (pm25 <= 12.0) return "Відмінно";
        if (pm25 <= 35.4) return "Задовільно";
        if (pm25 <= 55.4) return "Шкідливо";
        return "Небезпечно";
    }

    // Текстовий статус для PM10
    public String getPm10Status() {
        if (pm10 == null) return "Дані синхронізуються...";
        if (pm10 <= 50.0) return "Відмінно";
        if (pm10 <= 100.0) return "Задовільно";
        return "Шкідливо";
    }

    // Текстовий статус для NO2
    public String getNo2Status() {
        if (no2 == null) return "Дані синхронізуються...";
        if (no2 <= 40.0) return "Відмінно";
        if (no2 <= 80.0) return "Задовільно";
        return "Шкідливо";
    }

    // Текстовий статус для температури
    public String getTempStatus() {
        if (temperature == null) return "Дані синхронізуються...";
        if (temperature >= 15.0 && temperature <= 25.0) return "Комфортно";
        if ((temperature >= 0.0 && temperature < 15.0) || (temperature > 25.0 && temperature <= 30.0)) return "Помірно";
        return "Екстремально";
    }

    // Текстовий статус для вологості
    public String getHumidityStatus() {
        if (humidity == null) return "Дані синхронізуються...";
        if (humidity >= 40.0 && humidity <= 60.0) return "Комфортно";
        if ((humidity >= 20.0 && humidity < 40.0) || (humidity > 60.0 && humidity <= 80.0)) return "Помірно";
        return "Екстремально";
    }

    // Загальний інтегральний статус системи
    public String getOverallStatus() {
        if (pm25 == null || pm10 == null || no2 == null) return "Дані синхронізуються...";
        if (pm25 > 55.4) return "Небезпечно";
        if (pm25 > 35.4 || pm10 > 100.0 || no2 > 80.0) return "Шкідливо";
        if (pm25 > 12.0 || pm10 > 50.0 || no2 > 40.0) return "Задовільно";
        return "Відмінно";
    }

    // Колір для PM2.5
    public String getAqiColor() {
        if (pm25 == null) return "#475569"; // Нейтральний сірий
        if (pm25 <= 12.0) return "#10b981"; // Зелений
        if (pm25 <= 35.4) return "#f59e0b"; // Жовтий
        if (pm25 <= 55.4) return "#ef4444"; // Червоний
        return "#7f1d1d"; // Бордовий
    }

    // Колір для PM10
    public String getPm10Color() {
        if (pm10 == null) return "#475569";
        if (pm10 <= 50.0) return "#10b981";  // Зелений
        if (pm10 <= 100.0) return "#f59e0b"; // Жовтий
        return "#ef4444";                    // Червоний
    }

    // Колір для NO2
    public String getNo2Color() {
        if (no2 == null) return "#475569";
        if (no2 <= 40.0) return "#10b981";  // Зелений
        if (no2 <= 80.0) return "#f59e0b";  // Жовтий
        return "#ef4444";                   // Червоний
    }

    // Колір для температури
    public String getTempColor() {
        if (temperature == null) return "#475569";
        if (temperature >= 15.0 && temperature <= 25.0) return "#10b981"; // Зелений
        if ((temperature >= 0.0 && temperature < 15.0) || (temperature > 25.0 && temperature <= 30.0)) return "#3b82f6"; // Синій
        return "#ef4444"; // Червоний
    }

    // Колір для вологості
    public String getHumidityColor() {
        if (humidity == null) return "#475569";
        if (humidity >= 40.0 && humidity <= 60.0) return "#10b981"; // Зелений
        if ((humidity >= 20.0 && humidity < 40.0) || (humidity > 60.0 && humidity <= 80.0)) return "#3b82f6"; // Синій
        return "#ef4444"; // Червоний
    }

    // Загальний колір системи
    public String getOverallStatusColor() {
        if (pm25 == null || pm10 == null || no2 == null) return "#475569";
        String status = getOverallStatus();
        switch (status) {
            case "Відмінно": return "#10b981"; // Зелений
            case "Задовільно": return "#f59e0b"; // Жовтий
            case "Шкідливо": return "#ef4444"; // Червоний
            case "Небезпечно": return "#7f1d1d"; // Бордовий
            default: return "#475569";
        }
    }
}