package ua.edu.uzhnu.ecomonitor.service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.stereotype.Service;
import ua.edu.uzhnu.ecomonitor.model.Measurement;
import ua.edu.uzhnu.ecomonitor.repository.MeasurementRepository;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

@Service
public class ChartService {

    private final MeasurementRepository repository;

    public ChartService(MeasurementRepository repository) {
        this.repository = repository;
    }

    public byte[] createChartBytes(String type) throws IOException {
        // 1. Отримуємо останні 20 записів для більшої деталізації
        List<Measurement> measurements = repository.findTop20ByOrderByTimestampDesc();
        if (measurements.isEmpty()) return null;

        // 2. РЕВЕРС: щоб графік малювався зліва направо (від старих до нових)
        Collections.reverse(measurements);

        TimeSeries series = new TimeSeries(type);

        for (Measurement m : measurements) {
            if (m.getTimestamp() != null) {
                double value = getValueByParam(m, type.toLowerCase());
                // Використовуємо Second для точного відображення часу на осі X
                series.addOrUpdate(new Second(Timestamp.valueOf(m.getTimestamp())), value);
            }
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        String label = type.toUpperCase();
        String unit = getUnit(label);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Динаміка: " + label,
                "Час",
                unit,
                dataset,
                false, true, false
        );

        // --- Налаштування візуалізації ---
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // --- НАЛАШТУВАННЯ ТОЧНОСТІ ОСІ Y ---
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();

        // Формат числа: "0.00" — примусово показує 2 цифри після коми для точності
        rangeAxis.setNumberFormatOverride(new DecimalFormat("0.00"));

        // Фокусуємося на реальних значеннях (вісь не починається з 0, якщо значення близькі до 1.8)
        rangeAxis.setAutoRangeIncludesZero(false);

        // Мінімальний крок для шкали змінено на менший, щоб бачити мікро-зміни (наприклад, 1.81 -> 1.82)
        rangeAxis.setAutoRangeMinimumSize(0.01);

        // Дозволяємо шкалі підлаштовуватися автоматично під нові дані
        rangeAxis.setAutoRange(true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(baos, chart, 800, 400);
        return baos.toByteArray();
    }

    private String getUnit(String label) {
        if (label.contains("TEMP")) return "°C";
        if (label.contains("HUM")) return "%";
        return "мкг/м³";
    }

    private double getValueByParam(Measurement m, String param) {
        return switch (param.toLowerCase()) {
            case "pm25", "pm2.5" -> (m.getPm25() != null) ? m.getPm25() : 0.0;
            case "pm10" -> (m.getPm10() != null) ? m.getPm10() : 0.0;
            case "no2" -> (m.getNo2() != null) ? m.getNo2() : 0.0;
            case "temp", "temperature" -> (m.getTemperature() != null) ? m.getTemperature() : 0.0;
            case "hum", "humidity" -> (m.getHumidity() != null) ? m.getHumidity() : 0.0;
            default -> 0.0;
        };
    }
}