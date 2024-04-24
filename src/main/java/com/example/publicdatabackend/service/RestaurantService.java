package com.example.publicdatabackend.service;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.domain.statistics.CostsStatistics;
import com.example.publicdatabackend.domain.users.Users;
import com.example.publicdatabackend.dto.RestaurantDto;
import com.example.publicdatabackend.exception.UsersException;
import com.example.publicdatabackend.repository.*;
import com.example.publicdatabackend.utils.UsersErrorResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final UsersRepository usersRepository;
    private final RestaurantRepository restaurantRepository;
    private final KakaoReviewsRepository kakaoReviewsRepository;
    private final ReviewsRepository reviewsRepository;
    private final WishListRestaurantRepository wishListRestaurantRepository;
    private final CostsStatisticsRepository costsStatisticsRepository;

    /**
     * @param userId
     * @param pageable
     * @return Page<RestaurantDto>
     * @Description 매출수 Service Method
     */
    public Page<RestaurantDto> getRestaurantExecAmountsDescDTO(Long userId, Pageable pageable) {
        validateUser(userId);

        Page<Restaurant> restaurantPage = restaurantRepository.findAllByExecAmountsDesc(pageable);

        return buildRestaurantDto(restaurantPage, userId);
    }

    /**
     * @param userId
     * @param pageable
     * @return Page<RestaurantDto>
     * @Description 방문횟수 Service Method
     */
    public Page<RestaurantDto> getRestaurantNumberOfVisitDescDTO(Long userId, Pageable pageable) {
        validateUser(userId);

        Page<Restaurant> restaurantPage = restaurantRepository.findAllByNumberOfVisitDesc(pageable);

        return buildRestaurantDto(restaurantPage, userId);
    }

    /**
     * @param userId
     * @param price
     * @param pageable
     * @return Page<RestaurantDto>
     * @Description 가격별 Service Method
     */
    public Page<RestaurantDto> getRestaurantPriceDTO(Long userId, Long price, Pageable pageable) {
        validateUser(userId);

        Page<CostsStatistics> costsStatisticsPage = findCostsStatisticsByPrice(price, pageable);
        Page<Restaurant> restaurantPage = getRestaurantFromCostsStatistics(costsStatisticsPage, pageable);

        return buildRestaurantDto(restaurantPage, userId);
    }

    private Users validateUser(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new UsersException(UsersErrorResult.USER_ID_NOT_FOUND));
    }

    private Page<RestaurantDto> buildRestaurantDto(Page<Restaurant> restaurantPage, Long userId) {
        return restaurantPage.map(restaurant -> {
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

    private Page<CostsStatistics> findCostsStatisticsByPrice(Long price, Pageable pageable) {
        if (price <= 10000) {
            return costsStatisticsRepository.findByLower10000(pageable);
        } else if (price <= 15000) {
            return costsStatisticsRepository.findByLower15000(pageable);
        } else if (price <= 20000) {
            return costsStatisticsRepository.findByLower20000(pageable);
        } else {
            return costsStatisticsRepository.findByUpper20000(pageable);
        }
    }

    private Page<Restaurant> getRestaurantFromCostsStatistics(Page<CostsStatistics> costsStatisticsPage, Pageable pageable) {
        List<Long> restaurantIds = costsStatisticsPage.getContent().stream()
                .map(CostsStatistics::getRestaurantId)
                .collect(Collectors.toList());

        List<Restaurant> restaurants = restaurantRepository.findAllById(restaurantIds);
        return new PageImpl<>(restaurants, pageable, restaurants.size());
    }
}