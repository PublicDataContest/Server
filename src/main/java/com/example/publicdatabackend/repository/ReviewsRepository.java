package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.domain.reviews.Reviews;
import com.example.publicdatabackend.domain.users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
    @Query("select count(r) from Reviews r where r.restaurant = :restaurant")
    Long findReviewsNumByRestaurant(@Param("restaurant")Restaurant restaurant);

    Page<Reviews> findAllByUser(Users user, Pageable pageable);
}
