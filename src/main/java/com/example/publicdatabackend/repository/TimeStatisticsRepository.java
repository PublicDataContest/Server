package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.statistics.TimeStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeStatisticsRepository extends JpaRepository<TimeStatistics,Long> {
    Optional<TimeStatistics> findById(Long restaurantId);

    @Query("select t.restaurantId from TimeStatistics t order by t.hour9 desc")
    List<Long> getRestaurantIdByMorning();

    @Query("select t.restaurantId from TimeStatistics t order by t.hour12 desc")
    List<Long> getRestaurantIdByLunch();

    @Query("select t.restaurantId from TimeStatistics t order by t.hour18 desc")
    List<Long> getRestaurantIdByDinner();
}
