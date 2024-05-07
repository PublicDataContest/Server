package com.example.publicdatabackend.service;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.domain.statistics.CostsStatistics;
import com.example.publicdatabackend.domain.statistics.SeasonsStatistics;
import com.example.publicdatabackend.domain.users.Users;
import com.example.publicdatabackend.dto.map.CardDetailDto;
import com.example.publicdatabackend.dto.map.MapRestaurantDto;
import com.example.publicdatabackend.dto.restaurant.RestaurantDto;
import com.example.publicdatabackend.dto.restaurant.Top5RankingDto;
import com.example.publicdatabackend.exception.SeasonException;
import com.example.publicdatabackend.repository.*;
import com.example.publicdatabackend.utils.ErrorResult;
import com.example.publicdatabackend.utils.ExceptionUtils;
import com.example.publicdatabackend.utils.DtoConverterUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final CostsStatisticsRepository costsStatisticsRepository;
    private final TimeStatisticsRepository timeStatisticsRepository;
    private final SeasonStatisticsRepository seasonsRepository;
    private final DtoConverterUtils restaurantDtoConverterUtils;
    private final ExceptionUtils exceptionUtils;
    private final PeopleStatisticsRepository peopleStatisticsRepository;

    //메인화면 뷰
    public Page<MapRestaurantDto> viewCoordinateByLongText(Long userId, String searchText, Pageable pageable) {
        validateUser(userId);
        Page<Restaurant> restaurantPage = restaurantRepository.findAllByLongText(searchText, pageable);
        return buildMapRestaurantDto(restaurantPage, userId);
    }

    public Page<MapRestaurantDto> viewCoordinateByGpt(Long userId, String searchText, Pageable pageable) {
        validateUser(userId);
        // 랜덤으로 레스토랑을 선택하고 반환
        Page<Restaurant> restaurantPage = restaurantRepository.findRandomRestaurants(searchText, pageable);
        return buildMapRestaurantDto(restaurantPage, userId);
    }

    //각각 카드뷰
    public CardDetailDto getRestaurantDetails(Long userId, Long restaurantId) {
        validateUser(userId);
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        return buildCardRestaurantDto(restaurant, userId);
    }

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
     * @Description 평점순 Service Method
     */
    public Page<RestaurantDto> getRatingsListDTO(Long userId, Pageable pageable) {
        validateUser(userId);

        Page<Restaurant> restaurantPage = restaurantRepository.findAllByRatingDesc(pageable);

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
     * @param time
     * @param pageable
     * @return Page<RestaurantDto>
     * @Description 시간대별 Service Method
     */
    public Page<RestaurantDto> getRestaurantTimeDTO(Long userId, String time, Pageable pageable) {
        validateUser(userId);
        validateTime(time);
        Page<Restaurant> restaurantPage = null;

        if (time.equals("morning")) {
            List<Long> restaurantIds = timeStatisticsRepository.getRestaurantIdByMorning();
            restaurantPage = restaurantRepository.findAllByRestaurantIds(restaurantIds, pageable);
        }
        if (time.equals("lunch")) {
            List<Long> restaurantIds = timeStatisticsRepository.getRestaurantIdByLunch();
            restaurantPage = restaurantRepository.findAllByRestaurantIds(restaurantIds, pageable);
        }
        if (time.equals("dinner")) {
            List<Long> restaurantIds = timeStatisticsRepository.getRestaurantIdByDinner();
            restaurantPage = restaurantRepository.findAllByRestaurantIds(restaurantIds, pageable);
        }

        return buildRestaurantDto(restaurantPage, userId);
    }

    /**
     * @param userId
     * @param people
     * @param pageable
     * @return Page<RestaurantDto>
     * @Description 인원수별 Service Method
     */
    public Page<RestaurantDto> getRestaurantPeopleDTO(Long userId, Long people, Pageable pageable) {
        validateUser(userId);

        Page<Restaurant> restaurantPage = null;

        if (people <= 5) {
            List<Long> restaurantIds = peopleStatisticsRepository.findRestaurantIdsByLower5();
            restaurantPage = restaurantRepository.findAllByRestaurantIds(restaurantIds, pageable);
        } else if (people <= 10) {
            List<Long> restaurantIds = peopleStatisticsRepository.findRestaurantIdsByLower10();
            restaurantPage = restaurantRepository.findAllByRestaurantIds(restaurantIds, pageable);
        } else if (people <= 20) {
            List<Long> restaurantIds = peopleStatisticsRepository.findRestaurantIdsByLower20();
            restaurantPage = restaurantRepository.findAllByRestaurantIds(restaurantIds, pageable);
        } else {
            List<Long> restaurantIds = peopleStatisticsRepository.findRestaurantIdsByUpper20();
            restaurantPage = restaurantRepository.findAllByRestaurantIds(restaurantIds, pageable);
        }

        return buildRestaurantDto(restaurantPage, userId);
    }

    /**
     * @param userId
     * @return List<Restaurant>
     * @Description Top5 식당 Service Method
     */
    public List<Top5RankingDto> getRestaurantTopRankingListDTO(Long userId) {
        validateUser(userId);

        Page<Restaurant> restaurantPage = restaurantRepository.findAllByRatingDesc(PageRequest.of(0, 5));

        return buildTop5RankingDto(restaurantPage, userId).getContent();
    }

    /**
     * @param userId
     * @param longText
     * @param pageable
     * @return Page<RestaurantDto>
     * @Description 나를 위한 맞춤 맛집 Service Method
     */
    public Page<RestaurantDto> getRecommendationRestaurantDTO(Long userId, String longText, Pageable pageable) {
        validateUser(userId);

        Page<Restaurant> restaurantPage = restaurantRepository.findAllByLongTextOrderByRating(longText, pageable);

        return buildRestaurantDto(restaurantPage, userId);
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

    private Page<MapRestaurantDto> buildMapRestaurantDto(Page<Restaurant> restaurantPage, Long userId) {
        return restaurantPage.map(restaurant -> restaurantDtoConverterUtils.buildMapRestaurantDto(restaurant, userId));
    }

    private Page<Top5RankingDto> buildTop5RankingDto(Page<Restaurant> restaurantPage, Long userId) {
        return restaurantPage.map(restaurant -> restaurantDtoConverterUtils.buildTop5RankingDto(restaurant));
    }

    private CardDetailDto buildCardRestaurantDto(Optional<Restaurant> restaurant, Long userId) {
        if (restaurant.isPresent()) {
            return restaurantDtoConverterUtils.buildCardViewDto(restaurant.get(), userId);
        } else {
            return null;
        }
    }


    private Users validateUser(Long userId) {
        return exceptionUtils.validateUser(userId);
    }

    private void validateSeason(String season) {
        exceptionUtils.validateSeason(season);
    }

    private void validateTime(String time) {
        exceptionUtils.validateTime(time);
    }
    // <-- UTIL Method 선언부
}