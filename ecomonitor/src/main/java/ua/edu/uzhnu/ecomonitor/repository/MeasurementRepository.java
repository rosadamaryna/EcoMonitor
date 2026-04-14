package ua.edu.uzhnu.ecomonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.edu.uzhnu.ecomonitor.model.Measurement;

import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    /**
     * Отримання всіх записів екологічного моніторингу,
     * відсортованих за часом вимірювання від найдавнішого до найновішого.
     */
    List<Measurement> findAllByOrderByTimestampAsc();
}