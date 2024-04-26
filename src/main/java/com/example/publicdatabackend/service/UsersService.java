package com.example.publicdatabackend.service;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.domain.reviews.Reviews;
import com.example.publicdatabackend.domain.users.Users;
import com.example.publicdatabackend.domain.users.WishListRestaurant;
import com.example.publicdatabackend.dto.restaurant.RestaurantDto;
import com.example.publicdatabackend.repository.ReviewsRepository;
import com.example.publicdatabackend.repository.WishListRestaurantRepository;
import com.example.publicdatabackend.utils.ExceptionUtils;
import com.example.publicdatabackend.utils.RestaurantDtoConverterUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final WishListRestaurantRepository wishListRestaurantRepository;
    private final ExceptionUtils exceptionUtils;
    private final RestaurantDtoConverterUtils restaurantDtoConverterUtils;
    private final ReviewsRepository reviewsRepository;

    /**
     * @param userId
     * @param pageable
     * @return Page<RestaurantDto>
     * @Description 찜한 가게 Service Method
     */
    public Page<RestaurantDto> getWishRestaurant(Long userId, Pageable pageable) {
        Users user = validateUser(userId);

        Page<WishListRestaurant> wishListRestaurantPage = wishListRestaurantRepository.findWishListRestaurantByUser(user, pageable);
        Page<Restaurant> restaurantPage = getRestaurantFromWishListRestaurant(wishListRestaurantPage, pageable);

        return buildRestaurantDto(restaurantPage, userId);
    }

    public Page<RestaurantDto> getReviews(Long userId, Pageable pageable) {
        Users user = validateUser(userId);

        Page<Reviews> reviewsPage = reviewsRepository.findAllByUser(user, pageable);
        Page<Restaurant> restaurantPage = getRestaurantFromReviews(reviewsPage, pageable);

        return buildRestaurantDto(restaurantPage, userId);
    }


    // --> Domain -> Restaurant 변환 구간
    private Page<Restaurant> getRestaurantFromWishListRestaurant(Page<WishListRestaurant> wishListRestaurantPage, Pageable pageable) {
        List<Restaurant> restaurants = wishListRestaurantPage.getContent().stream()
                .map(WishListRestaurant::getRestaurant)
                .collect(Collectors.toList());

        return new PageImpl<>(restaurants, pageable, restaurants.size());
    }

    private Page<Restaurant> getRestaurantFromReviews(Page<Reviews> wishListRestaurantPage, Pageable pageable) {
        List<Restaurant> restaurants = wishListRestaurantPage.getContent().stream()
                .map(Reviews::getRestaurant)
                .collect(Collectors.toList());

        return new PageImpl<>(restaurants, pageable, restaurants.size());
    }
    // <-- Domain -> Restaurant 변환 구간

    // --> UTIL Method 선언부
    private Users validateUser(Long userId) {
        return exceptionUtils.validateUser(userId);
    }

    private Page<RestaurantDto> buildRestaurantDto(Page<Restaurant> restaurantPage, Long userId) {
        return restaurantPage.map(restaurant -> restaurantDtoConverterUtils.buildRestaurantDto(restaurant, userId));
    }
    // <-- UTIL Method 선언부
}
