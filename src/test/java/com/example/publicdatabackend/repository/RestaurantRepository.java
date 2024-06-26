package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r FROM Restaurant r ORDER BY r.totalExecAmounts DESC")
    List<Restaurant> findAllByExecAmountsDesc();

    @Query("SELECT r FROM Restaurant r ORDER BY r.numberOfVisit DESC")
    List<Restaurant> findAllByNumberOfVisitDesc();
}
