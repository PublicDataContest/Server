package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.statistics.CostsStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CostsStatisticsRepository extends JpaRepository<CostsStatistics, Long> {
    @Query("SELECT c FROM CostsStatistics c WHERE c.lower10000 > 0 ORDER BY c.lower10000 DESC")
    Page<CostsStatistics> findByLower10000(Pageable pageable);

    @Query("SELECT c FROM CostsStatistics c WHERE c.lower15000 > 0 ORDER BY c.lower15000 DESC")
    Page<CostsStatistics> findByLower15000(Pageable pageable);

    @Query("SELECT c FROM CostsStatistics c WHERE c.lower20000 > 0 ORDER BY c.lower20000 DESC")
    Page<CostsStatistics> findByLower20000(Pageable pageable);

    @Query("SELECT c FROM CostsStatistics c WHERE c.upper20000 > 0 ORDER BY c.upper20000 DESC")
    Page<CostsStatistics> findByUpper20000(Pageable pageable);

    Optional<CostsStatistics> findById(Long restaurantId);
}
