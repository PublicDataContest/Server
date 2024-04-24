package com.example.publicdatabackend.utils;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.dto.RestaurantDto;
import com.example.publicdatabackend.repository.KakaoReviewsRepository;
import com.example.publicdatabackend.repository.ReviewsRepository;
import com.example.publicdatabackend.repository.WishListRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantDtoConverterUtils {
    private final KakaoReviewsRepository kakaoReviewsRepository;
    private final ReviewsRepository reviewsRepository;
    private final WishListRestaurantRepository wishListRestaurantRepository;

    public RestaurantDto buildRestaurantDto(Restaurant restaurant, Long userId) {
        Long kakaoReviewsNum = kakaoReviewsRepository.findKakaoReviewsNumByRestaurant(restaurant);
        Long reviewsNum = reviewsRepository.findReviewsNumByRestaurant(restaurant);

        Boolean wishListRestaurant = wishListRestaurantRepository
                .findWishListRestaurantByUserIdAndRestaurantId(userId, restaurant.getId())
                .isPresent();

        return RestaurantDto.builder()
                .restaurantId(restaurant.getId())
                .placeName(restaurant.getPlaceName())
                .reviewsNum(kakaoReviewsNum + reviewsNum)
                .rating(restaurant.getRating())
                .wishListRestaurant(wishListRestaurant)
                .currentOpeningHours(restaurant.getCurrentOpeningHours())
                .photoUrl(restaurant.getPhotoUrl())
                .build();
    }
}
