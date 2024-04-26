package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.statistics.TimeStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeStatisticsRepository extends JpaRepository<TimeStatistics,Long> {
    Optional<TimeStatistics> findById(Long restaurantId);
}
