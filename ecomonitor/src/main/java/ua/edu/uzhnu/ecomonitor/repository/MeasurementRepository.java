package ua.edu.uzhnu.ecomonitor.repository;

import ua.edu.uzhnu.ecomonitor.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    //Вибірка 20 останніх записів ДЛЯ КОНКРЕТНОЇ ЛОКАЦІЇ
    List<Measurement> findTop20ByLocationNameOrderByTimestampDesc(String locationName);

    // Вибірка 20 останніх записів для побудови графіків на головній сторінці
    List<Measurement> findTop20ByOrderByTimestampDesc();

    // Вибірка всіх записів у зворотному порядку для сторінки історії
    List<Measurement> findAllByOrderByTimestampDesc();
}