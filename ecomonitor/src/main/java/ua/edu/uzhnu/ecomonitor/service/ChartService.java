package ua.edu.uzhnu.ecomonitor.service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;
import ua.edu.uzhnu.ecomonitor.repository.MeasurementRepository;

@Service
public class ChartService {

    private final MeasurementRepository repository;

    // Впроваджуємо залежність репозиторію через конструктор
    public ChartService(MeasurementRepository repository) {
        this.repository = repository;
    }

    /**
     * Метод-заглушка для генерації байтового масиву графіка.
     * Реалізацію вибірки даних за 7 днів та експорт у формат PNG додамо в наступному коміті.
     */
    public byte[] generateChartImage() {
        System.out.println("ChartService: Preparing dataset for environmental charts...");

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Тимчасові тестові дані для перевірки ініціалізації JFreeChart
        dataset.addValue(15.0, "Температура", "Тест");

        JFreeChart chart = ChartFactory.createLineChart(
                "Екологічний моніторинг",
                "Час",
                "Значення",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        System.out.println("ChartService: Blank chart engine generated successfully.");
        return new byte[0]; // Поки повертаємо порожній масив
    }
}