package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.statistics.PeopleStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleStatisticsRepository extends JpaRepository<PeopleStatistics, Long> {
    Optional<PeopleStatistics> findById(Long restaurantId);

    @Query("SELECT p.restaurantId FROM PeopleStatistics p ORDER BY p.lower5 DESC")
    List<Long> findRestaurantIdsByLower5();

    @Query("SELECT p.restaurantId FROM PeopleStatistics p ORDER BY p.lower10 DESC")
    List<Long> findRestaurantIdsByLower10();

    @Query("SELECT p.restaurantId FROM PeopleStatistics p ORDER BY p.lower20 DESC")
    List<Long> findRestaurantIdsByLower20();

    @Query("SELECT p.restaurantId FROM PeopleStatistics p ORDER BY p.upper20 DESC")
    List<Long> findRestaurantIdsByUpper20();
}
