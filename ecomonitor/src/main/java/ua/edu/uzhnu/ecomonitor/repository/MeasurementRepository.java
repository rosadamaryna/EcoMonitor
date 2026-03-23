package ua.edu.uzhnu.ecomonitor.repository;

import ua.edu.uzhnu.ecomonitor.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
}