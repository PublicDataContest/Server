package com.example.publicdatabackend.service;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.dto.RestaurantDto;
import com.example.publicdatabackend.repository.KakaoReviewsRepository;
import com.example.publicdatabackend.repository.RestaurantRepository;
import com.example.publicdatabackend.repository.ReviewsRepository;
import com.example.publicdatabackend.repository.WishListRestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final KakaoReviewsRepository kakaoReviewsRepository;
    private final ReviewsRepository reviewsRepository;
    private final WishListRestaurantRepository wishListRestaurantRepository;

    public Page<RestaurantDto> getRestaurantExecAmountsDescDTO(Long userId, Pageable pageable) {
        // Page<Restaurant> 객체를 가져옴
        Page<Restaurant> page = restaurantRepository.findAllByExecAmountsDesc(pageable);

        // DTO 변환
        return page.map(restaurant -> {
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
        });
    }
}