package ua.edu.uzhnu.ecomonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.edu.uzhnu.ecomonitor.model.Measurement;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
}