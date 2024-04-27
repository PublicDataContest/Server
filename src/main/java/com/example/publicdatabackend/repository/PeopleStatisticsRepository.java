package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.statistics.PeopleStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleStatisticsRepository extends JpaRepository<PeopleStatistics, Long> {
    Optional<PeopleStatistics> findById(Long restaurantId);
}
