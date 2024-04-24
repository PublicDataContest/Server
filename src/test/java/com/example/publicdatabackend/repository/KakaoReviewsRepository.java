package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.Restaurant;
import com.example.publicdatabackend.domain.review.KakaoReviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface KakaoReviewsRepository extends JpaRepository<KakaoReviews, Long> {
    @Query("SELECT count(kr) FROM KakaoReviews kr WHERE kr.restaurant = :restaurant")
    Long findKakaoReviewsNumByRestaurant(@Param("restaurant") Restaurant restaurant);
}
