package com.example.publicdatabackend.service;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.domain.users.Users;
import com.example.publicdatabackend.domain.users.WishListRestaurant;
import com.example.publicdatabackend.repository.RestaurantRepository;
import com.example.publicdatabackend.repository.UserRepository;
import com.example.publicdatabackend.repository.WishListRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class WishListService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private WishListRestaurantRepository wishListRestaurantRepository;

    @Transactional
    public Optional<WishListRestaurant> toggleWishList(Long userId, Long restaurantId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Optional<WishListRestaurant> existingWish = wishListRestaurantRepository.findByUserAndRestaurant(user, restaurant);

        if (existingWish.isPresent()) {
            wishListRestaurantRepository.delete(existingWish.get());
            return Optional.empty();
        } else {
            WishListRestaurant newWish = WishListRestaurant.builder()
                    .user(user)
                    .restaurant(restaurant)
                    .build();
            return Optional.of(wishListRestaurantRepository.save(newWish));
        }
    }
}

