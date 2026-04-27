package com.maryna.ecomonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maryna.ecomonitor.model.Measurement;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    // Тут ми поки нічого не пишемо, JpaRepository вже має всі методи для роботи з БД
}