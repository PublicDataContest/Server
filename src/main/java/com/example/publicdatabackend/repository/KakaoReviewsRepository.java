package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.domain.reviews.KakaoReviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KakaoReviewsRepository extends JpaRepository<KakaoReviews, Long> {
    @Query("SELECT count(kr) FROM KakaoReviews kr WHERE kr.restaurant = :restaurant")
    Long findKakaoReviewsNumByRestaurant(@Param("restaurant") Restaurant restaurant);

    List<KakaoReviews> findByRestaurantId(Long restaurantId);
}
