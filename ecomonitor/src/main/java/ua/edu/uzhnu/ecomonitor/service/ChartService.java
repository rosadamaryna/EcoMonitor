package ua.edu.uzhnu.ecomonitor.service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;
import ua.edu.uzhnu.ecomonitor.model.Measurement;
import ua.edu.uzhnu.ecomonitor.repository.MeasurementRepository;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ChartService {

    private final MeasurementRepository repository;

    public ChartService(MeasurementRepository repository) {
        this.repository = repository;
    }

    /**
     * Генерація лінійного графіка динаміки екологічних показників.
     * Метод вибирає дані з репозиторію, формує набір даних JFreeChart
     * та експортує результат у вигляді байтового масиву PNG-зображення.
     */
    public byte[] generateChartImage() {
        System.out.println("ChartService: Starting generation pipeline for 7-day metrics...");

        // Створюємо контейнер для даних графіка
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Завантажуємо всі вимірювання з бази даних
        List<Measurement> measurements = repository.findAll();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM HH:mm");

        // Обмеження вибірки для графіка (беремо останні записи, якщо їх багато)
        int startIdx = Math.max(0, measurements.size() - 24); // Відображаємо останні 24 точки для чіткості

        for (int i = startIdx; i < measurements.size(); i++) {
            Measurement m = measurements.get(i);

            // Захист від null значень дати або показників
            String timeLabel = (m.getTimestamp() != null) ? m.getTimestamp().format(formatter) : "00.00 00:00";

            double pm25Value = (m.getPm25() != null) ? m.getPm25() : 0.0;
            double tempValue = (m.getTemperature() != null) ? m.getTemperature() : 0.0;

            // Додаємо лінії на графік
            dataset.addValue(pm25Value, "PM2.5 (мкг/м³)", timeLabel);
            dataset.addValue(tempValue, "Температура (°C)", timeLabel);
        }

        // Ініціалізація та налаштування лінійного графіка
        JFreeChart chart = ChartFactory.createLineChart(
                "Динаміка екологічних та метеорологічних показників", // Заголовок
                "Час вимірювання",                                    // Підпис осі X
                "Значення метрик",                                    // Підпис осі Y
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Конвертація об'єкта графіка у формат PNG через ByteArrayOutputStream
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(baos, chart, 800, 400);
            System.out.println("ChartService: PNG binary stream generated successfully. Size: " + baos.size() + " bytes");
            return baos.toByteArray();
        } catch (Exception e) {
            System.err.println("Error during JFreeChart PNG export operation: " + e.getMessage());
            return new byte[0];
        }
    }
}