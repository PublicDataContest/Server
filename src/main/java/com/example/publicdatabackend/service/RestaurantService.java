package com.example.publicdatabackend.service;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.domain.statistics.CostsStatistics;
import com.example.publicdatabackend.domain.statistics.SeasonsStatistics;
import com.example.publicdatabackend.domain.users.Users;
import com.example.publicdatabackend.dto.RestaurantDto;
import com.example.publicdatabackend.exception.SeasonException;
import com.example.publicdatabackend.repository.*;
import com.example.publicdatabackend.utils.ErrorResult;
import com.example.publicdatabackend.utils.ExceptionUtils;
import com.example.publicdatabackend.utils.RestaurantDtoConverterUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final CostsStatisticsRepository costsStatisticsRepository;
    private final SeasonsRepository seasonsRepository;
    private final RestaurantDtoConverterUtils restaurantDtoConverterUtils;
    private final ExceptionUtils exceptionUtils;

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

    /**
     * @param userId
     * @param season
     * @param pageable
     * @return Page<RestaurantDto>
     * @Description 계절별 Service Method
     */
    public Page<RestaurantDto> getRestaurantSeasonDTO(Long userId, String season, Pageable pageable) {
        validateUser(userId);
        validateSeason(season);

        Page<SeasonsStatistics> seasonsStatistics = findSeasonsStatisticsBySeason(season, pageable);
        Page<Restaurant> restaurantPage = getRestaurantFromSeasonsStatistics(seasonsStatistics, pageable);

        return buildRestaurantDto(restaurantPage, userId);
    }

    /**
     * @param userId
     * @return List<Restaurant>
     * @Description Top5 식당 Service Method
     */
    public List<RestaurantDto> getRestaurantTopRankingListDTO(Long userId) {
        validateUser(userId);

        Page<Restaurant> restaurantPage = restaurantRepository.findAllByRatingDesc(PageRequest.of(0, 5));

        return buildRestaurantDto(restaurantPage, userId).getContent();
    }

    // --> Statistics 반환 구간
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

    private Page<SeasonsStatistics> findSeasonsStatisticsBySeason(String season, Pageable pageable) {
        if (season.equals("spring")) {
            return seasonsRepository.findBySpringDesc(pageable);
        } else if (season.equals("summer")) {
            return seasonsRepository.findBySummerDesc(pageable);
        } else if (season.equals("fall")) {
            return seasonsRepository.findByFallDesc(pageable);
        } else if (season.equals("winter")) {
            return seasonsRepository.findByWinterDesc(pageable);
        } else {
            throw new SeasonException(ErrorResult.UNKNOWN_EXCEPTION);
        }
    }
    // <-- Statistics 반환 구간


    // --> Statistics -> Restaurant 변환 구간
    private Page<Restaurant> getRestaurantFromCostsStatistics(Page<CostsStatistics> costsStatisticsPage, Pageable pageable) {
        List<Long> restaurantIds = costsStatisticsPage.getContent().stream()
                .map(CostsStatistics::getRestaurantId)
                .collect(Collectors.toList());

        List<Restaurant> restaurants = restaurantRepository.findAllById(restaurantIds);
        return new PageImpl<>(restaurants, pageable, restaurants.size());
    }

    private Page<Restaurant> getRestaurantFromSeasonsStatistics(Page<SeasonsStatistics> seasonsStatistics, Pageable pageable) {
        List<Long> restaurantIds = seasonsStatistics.getContent().stream()
                .map(SeasonsStatistics::getRestaurantId)
                .collect(Collectors.toList());

        List<Restaurant> restaurants = restaurantRepository.findAllById(restaurantIds);
        return new PageImpl<>(restaurants, pageable, restaurants.size());
    }
    // <-- Statistics -> Restaurant 변환 구간

    // --> UTIL Method 선언부
    private Page<RestaurantDto> buildRestaurantDto(Page<Restaurant> restaurantPage, Long userId) {
        return restaurantPage.map(restaurant -> restaurantDtoConverterUtils.buildRestaurantDto(restaurant, userId));
    }

    private Users validateUser(Long userId) {
        return exceptionUtils.validateUser(userId);
    }

    private void validateSeason(String season) {
        exceptionUtils.validateSeason(season);
    }
    // <-- UTIL Method 선언부
}