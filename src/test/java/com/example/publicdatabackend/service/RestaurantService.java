package com.example.publicdatabackend.service;

import com.example.publicdatabackend.domain.Restaurant;
import com.example.publicdatabackend.dto.RestaurantDto;
import com.example.publicdatabackend.repository.KakaoReviewsRepository;
import com.example.publicdatabackend.repository.RestaurantRepository;
import com.example.publicdatabackend.repository.ReviewsRepository;
import com.example.publicdatabackend.repository.WishListRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final KakaoReviewsRepository kakaoReviewsRepository;
    private final ReviewsRepository reviewsRepository;
    private final WishListRestaurantRepository wishListRestaurantRepository;

    public List<RestaurantDto.RestaurantExecAmounts> getRestaurantExecAmountsDescDTO(Long userId) {
        // ExecAmounts 역순
        List<Restaurant> restaurantsDescs = restaurantRepository.findAllByExecAmountsDesc();

        // 반환 DTO 생성
        List<RestaurantDto.RestaurantExecAmounts> restaurantExecAmountsList = new ArrayList<>();

        // 역순으로 정렬된 List 순회
        for(Restaurant restaurantDesc : restaurantsDescs) {
            // 해당 Restuarant의 Reviews 반환
            Long kakaoReviewsNum = kakaoReviewsRepository.findKakaoReviewsNumByRestaurant(restaurantDesc);
            Long reviewsNum = reviewsRepository.findReviewsNumByRestaurant(restaurantDesc);

            // 사용자가 해당 restaurant를 좋아요
            Boolean wishListRestaurant
                    = wishListRestaurantRepository.findWishListRestaurantByUserIdAndRestaurantId(userId, restaurantDesc.getId()).isEmpty() ? false : true;

            // DTO 생성
            RestaurantDto.RestaurantExecAmounts restaurantExecAmounts
                    = RestaurantDto.RestaurantExecAmounts.builder()
                    .restaurantId(restaurantDesc.getId())
                    .placeName(restaurantDesc.getPlaceName())
                    .reviewsNum(kakaoReviewsNum + reviewsNum)
                    .rating(restaurantDesc.getRating())
                    .wishListRestaurant(wishListRestaurant)
                    .currentOpeningHours(restaurantDesc.getCurrentOpeningHours())
                    .photoUrl(restaurantDesc.getPhotoUrl())
                    .build();

            restaurantExecAmountsList.add(restaurantExecAmounts);
        }

        return restaurantExecAmountsList;
    }
}
