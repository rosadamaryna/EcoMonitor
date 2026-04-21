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

    // --- Геттери та сеттери ---

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

    /**
     * Визначення індексу якості повітря (AQI) на основі концентрації часток PM2.5.
     * Повертає текстовий статус для відображення на сторінці моніторингу.
     */
    public String getPm25Status() {
        if (pm25 == null) return "Немає даних";
        if (pm25 <= 12.0) return "Відмінно";
        if (pm25 <= 35.4) return "Задовільно";
        if (pm25 <= 55.4) return "Шкідливо";
        return "Небезпечно";
    }

    /**
     * Комплексна оцінка екологічної ситуації за всіма наявними маркерами забруднення.
     * Статус визначається за найгіршим значенням серед показників PM2.5, PM10 та NO2.
     */
    public String getOverallStatus() {
        // Перевірка на наявність даних для уникнення NullPointerException
        if (pm25 == null || pm10 == null || no2 == null) return "Оновлення...";

        // Логіка оцінки стану за найгіршим параметром
        if (pm25 > 55.4 || pm10 > 150 || no2 > 100) return "Небезпечно";
        if (pm25 > 35.4 || pm10 > 100 || no2 > 80) return "Шкідливо";
        if (pm25 > 12.0 || pm10 > 50 || no2 > 40) return "Задовільно";

        return "Відмінно";
    }

    // --- Методи динамічного підбору кольорів для веб-інтерфейсу (HEX-коди) ---

    /**
     * Повертає колір для картки PM2.5 залежно від рівня забруднення.
     */
    public String getPm25Color() {
        if (pm25 == null) return "#64748b"; // Сірий за замовчуванням
        if (pm25 <= 12.0) return "#10b981"; // Зелений (Відмінно)
        if (pm25 <= 35.4) return "#f59e0b"; // Жовтий (Задовільно)
        if (pm25 <= 55.4) return "#f97316"; // Помаранчевий (Шкідливо)
        return "#ef4444"; // Червоний (Небезпечно)
    }

    /**
     * Повертає колір для картки PM10.
     */
    public String getPm10Color() {
        if (pm10 == null) return "#64748b";
        if (pm10 <= 50.0) return "#10b981"; // Норма
        if (pm10 <= 100.0) return "#f59e0b"; // Підвищено
        return "#ef4444"; // Небезпечно
    }

    /**
     * Повертає колір для картки діоксиду азоту NO2.
     */
    public String getNo2Color() {
        if (no2 == null) return "#64748b";
        if (no2 <= 40.0) return "#10b981"; // Чисто
        if (no2 <= 80.0) return "#f59e0b"; // Помірне забруднення
        return "#ef4444"; // Високе забруднення
    }

    /**
     * Повертає колір для температурного віджета залежно від градусів.
     */
    public String getTempColor() {
        if (temperature == null) return "#64748b";
        if (temperature > 25.0) return "#ef4444"; // Спекотно (Червоний)
        if (temperature > 15.0) return "#10b981"; // Комфортно (Зелений)
        return "#3b82f6"; // Прохолодно/Холодно (Синій)
    }
}