package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.users.WishListRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishListRestaurantRepository extends JpaRepository<WishListRestaurant, Long> {
    @Query("select w from WishListRestaurant w where w.user.id =: userId and w.restaurant.id =: restaurantId")
    Optional<WishListRestaurant> findWishListRestaurantByUserIdAndRestaurantId(@Param("userId") Long userId,
                                                                               @Param("restaurantId") Long restaurantId);
}
