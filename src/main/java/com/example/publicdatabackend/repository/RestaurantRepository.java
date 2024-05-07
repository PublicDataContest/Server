package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r FROM Restaurant r ORDER BY r.totalExecAmounts DESC")
    Page<Restaurant> findAllByExecAmountsDesc(Pageable pageable);

    @Query("SELECT r FROM Restaurant r ORDER BY r.numberOfVisit DESC")
    Page<Restaurant> findAllByNumberOfVisitDesc(Pageable pageable);

    @Query("SELECT r FROM Restaurant r ORDER BY r.rating DESC")
    Page<Restaurant> findAllByRatingDesc(Pageable pageable);

    Page<Restaurant> findAllByLongText(String searchText, Pageable pageable);

    Optional<Restaurant> findById(Long restaurantId);

    @Query(value = "SELECT * FROM restaurant ORDER BY RANDOM()", nativeQuery = true)
    Page<Restaurant> findRandomRestaurants(String searchText, Pageable pageable);


    @Query("SELECT r FROM Restaurant r WHERE r.id IN (:restaurantIds)")
    Page<Restaurant> findAllByRestaurantIds(@Param("restaurantIds") List<Long> restaurantIds, Pageable pageable);

    @Query("SELECT r FROM Restaurant r WHERE r.longText = :longText ORDER BY r.rating DESC")
    Page<Restaurant> findAllByLongTextOrderByRating(@Param("longText") String longText, Pageable pageable);
}
