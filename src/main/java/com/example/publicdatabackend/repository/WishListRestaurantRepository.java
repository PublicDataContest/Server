package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.domain.users.Users;
import com.example.publicdatabackend.domain.users.WishListRestaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRestaurantRepository extends JpaRepository<WishListRestaurant, Long> {
    @Query("select w from WishListRestaurant w where w.user.id = :userId and w.restaurant.id = :restaurantId")
    Optional<WishListRestaurant> findWishListRestaurantByUserIdAndRestaurantId(@Param("userId") Long userId,
                                                                               @Param("restaurantId") Long restaurantId);
    Optional<WishListRestaurant> findByUserAndRestaurant(Users user, Restaurant restaurant);

    Page<WishListRestaurant> findWishListRestaurantByUser(Users users, Pageable pageable);
}
