package com.example.publicdatabackend.service;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.domain.users.Users;
import com.example.publicdatabackend.dto.RestaurantDto;
import com.example.publicdatabackend.exception.UsersException;
import com.example.publicdatabackend.repository.*;
import com.example.publicdatabackend.utils.UsersErrorResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final UsersRepository usersRepository;
    private final RestaurantRepository restaurantRepository;
    private final KakaoReviewsRepository kakaoReviewsRepository;
    private final ReviewsRepository reviewsRepository;
    private final WishListRestaurantRepository wishListRestaurantRepository;

    /**
     * @param userId
     * @param pageable
     * @return Page<RestaurantDto>
     * @Description 매출수 Service Method
     */
    public Page<RestaurantDto> getRestaurantExecAmountsDescDTO(Long userId, Pageable pageable) {
        validateUser(userId);

        Page<Restaurant> page = restaurantRepository.findAllByExecAmountsDesc(pageable);

        return buildRestaurantDto(page, userId);
    }

    /**
     * @param userId
     * @param pageable
     * @return Page<RestaurantDto>
     * @Description 방문횟수 Service Method
     */
    public Page<RestaurantDto> getRestaurantNumberOfVisitDescDTO(Long userId, Pageable pageable) {
        validateUser(userId);

        Page<Restaurant> page = restaurantRepository.findAllByNumberOfVisitDesc(pageable);

        return buildRestaurantDto(page, userId);
    }

    private Users validateUser(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new UsersException(UsersErrorResult.USER_ID_NOT_FOUND));
    }

    private Page<RestaurantDto> buildRestaurantDto(Page<Restaurant> page, Long userId) {
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