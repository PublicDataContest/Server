package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.statistics.SeasonsStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeasonStatisticsRepository extends JpaRepository<SeasonsStatistics, Long> {
    @Query("SELECT s FROM SeasonsStatistics s WHERE s.spring > 0 ORDER BY s.spring DESC")
    Page<SeasonsStatistics> findBySpringDesc(Pageable pageable);

    @Query("SELECT s FROM SeasonsStatistics s WHERE s.summer > 0 ORDER BY s.spring DESC")
    Page<SeasonsStatistics> findBySummerDesc(Pageable pageable);

    @Query("SELECT s FROM SeasonsStatistics s WHERE s.fall > 0 ORDER BY s.spring DESC")
    Page<SeasonsStatistics> findByFallDesc(Pageable pageable);

    @Query("SELECT s FROM SeasonsStatistics s WHERE s.winter > 0 ORDER BY s.spring DESC")
    Page<SeasonsStatistics> findByWinterDesc(Pageable pageable);

    Optional<SeasonsStatistics> findById(Long restaurantId);
}
