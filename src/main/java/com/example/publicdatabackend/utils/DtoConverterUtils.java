package com.example.publicdatabackend.utils;

import com.example.publicdatabackend.domain.restaurant.Menu;
import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.domain.reviews.KakaoReviews;
import com.example.publicdatabackend.domain.reviews.Reviews;
import com.example.publicdatabackend.domain.statistics.CostsStatistics;
import com.example.publicdatabackend.domain.statistics.PeopleStatistics;
import com.example.publicdatabackend.domain.statistics.SeasonsStatistics;
import com.example.publicdatabackend.domain.statistics.TimeStatistics;
import com.example.publicdatabackend.domain.users.Users;
import com.example.publicdatabackend.dto.review.KakaoReviewDto;
import com.example.publicdatabackend.dto.review.NormalReviewDto;
import com.example.publicdatabackend.dto.review.ReviewDto;
import com.example.publicdatabackend.dto.map.CardDetailDto;
import com.example.publicdatabackend.dto.map.MapRestaurantDto;
import com.example.publicdatabackend.dto.menu.MenuListDto;
import com.example.publicdatabackend.dto.restaurant.RestaurantDto;
import com.example.publicdatabackend.dto.statistics.PeopleStatisticsDto;
import com.example.publicdatabackend.dto.statistics.PriceStatisticsDto;
import com.example.publicdatabackend.dto.statistics.SeasonStatisticsDto;
import com.example.publicdatabackend.dto.statistics.TimeStatisticsDto;
import com.example.publicdatabackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class DtoConverterUtils {
    private final KakaoReviewsRepository kakaoReviewsRepository;
    private final ReviewsRepository reviewsRepository;
    private final WishListRestaurantRepository wishListRestaurantRepository;
    private final CategoryRepository categoryRepository;
    private final ExceptionUtils exceptionUtils;

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

    //좌표와 밑에 카드 리스트를 위한 dto 변환
    public MapRestaurantDto buildMapRestaurantDto(Restaurant restaurant, Long userId) {
        Long kakaoReviewsNum = kakaoReviewsRepository.findKakaoReviewsNumByRestaurant(restaurant);
        Long reviewsNum = reviewsRepository.findReviewsNumByRestaurant(restaurant);

        Boolean wishListRestaurant = wishListRestaurantRepository
                .findWishListRestaurantByUserIdAndRestaurantId(userId, restaurant.getId())
                .isPresent();

        // 카테고리 정보 가져오기
        List<Object[]> categoryDetails = categoryRepository.findCategoryDetailsByRestaurant(restaurant);
        String categoryName = categoryDetails.isEmpty() ? "" : (String) categoryDetails.get(0)[0];
        String hashTags = categoryDetails.isEmpty() ? "" : (String) categoryDetails.get(0)[1];
        List<String> extractedWords = extractKoreanWords(categoryName + " " + hashTags);

        return MapRestaurantDto.builder()
                .restaurantId(restaurant.getId())
                .placeName(restaurant.getPlaceName())
                .reviewsNum(kakaoReviewsNum + reviewsNum)
                .photoUrl(restaurant.getPhotoUrl())
                .rating(restaurant.getRating())
                .x(restaurant.getX())
                .y(restaurant.getY())
                .wishListRestaurant(wishListRestaurant)
                .categoryName(categoryName)  // categoryName 추가
                .hashTags(hashTags)          // hashTags 추가
                .category(extractedWords)
                .build();
    }

    //각각 카드뷰를 위한 dto 변환
    public CardDetailDto buildCardViewDto(Restaurant restaurant, Long userId){
        Long kakaoReviewsNum = kakaoReviewsRepository.findKakaoReviewsNumByRestaurant(restaurant);
        Long reviewsNum = reviewsRepository.findReviewsNumByRestaurant(restaurant);

        Boolean wishListRestaurant = wishListRestaurantRepository
                .findWishListRestaurantByUserIdAndRestaurantId(userId, restaurant.getId())
                .isPresent();

        return CardDetailDto.builder()
                .restaurantId(restaurant.getId())
                .placeName(restaurant.getPlaceName())
                .addressName(restaurant.getAddressName())
                .currentOpeningHours(restaurant.getCurrentOpeningHours())
                .phone(restaurant.getPhone())
                .rating(restaurant.getRating())
                .photoUrl(restaurant.getPhotoUrl())
                .reviewsNum(reviewsNum+kakaoReviewsNum)
                .wishListRestaurant(wishListRestaurant)
                .build();
    }

    public MenuListDto buildMenuDto(Menu menu, Long restaurantId) {
        return MenuListDto.builder()
                .menu(menu.getMenu())
                .price(menu.getPrice())
                .build();
    }

    public PeopleStatisticsDto buildPeopleDto(Optional<PeopleStatistics> peopleStatistics){
        return PeopleStatisticsDto.builder()
                .lower5(peopleStatistics.get().getLower5())
                .lower10(peopleStatistics.get().getLower10())
                .lower20(peopleStatistics.get().getLower20())
                .upper20(peopleStatistics.get().getUpper20())
                .build();
    }

    public TimeStatisticsDto buildTimeDto(Optional<TimeStatistics> timeStatistics){
        return TimeStatisticsDto.builder()
                .hour8(timeStatistics.get().getHour8())
                .hour9(timeStatistics.get().getHour9())
                .hour10(timeStatistics.get().getHour10())
                .hour11(timeStatistics.get().getHour11())
                .hour12(timeStatistics.get().getHour12())
                .hour13(timeStatistics.get().getHour13())
                .hour14(timeStatistics.get().getHour14())
                .hour15(timeStatistics.get().getHour15())
                .hour16(timeStatistics.get().getHour16())
                .hour17(timeStatistics.get().getHour17())
                .hour18(timeStatistics.get().getHour18())
                .hour19(timeStatistics.get().getHour19())
                .hour20(timeStatistics.get().getHour20())
                .hour21(timeStatistics.get().getHour21())
                .build();

    }
    public SeasonStatisticsDto buildSeasonDto(Optional<SeasonsStatistics> seasonsStatistics){
        return SeasonStatisticsDto.builder()
                .spring(seasonsStatistics.get().getSpring())
                .summer(seasonsStatistics.get().getSummer())
                .fall(seasonsStatistics.get().getFall())
                .winter(seasonsStatistics.get().getWinter())
                .build();

    }
    public PriceStatisticsDto buildCostDto(Optional<CostsStatistics> costsStatistics){
        return PriceStatisticsDto.builder()
                .lower10000(costsStatistics.get().getLower10000())
                .lower15000(costsStatistics.get().getLower15000())
                .lower20000(costsStatistics.get().getLower20000())
                .upper20000(costsStatistics.get().getUpper20000())
                .build();
    }


    public KakaoReviewDto buildKakaoReviewDto(KakaoReviews kakaoReview) {
        return KakaoReviewDto.builder()
                .id(kakaoReview.getId())
                .authorName(kakaoReview.getAuthorName())
                .rating(kakaoReview.getRating())
                .relativeTimeDescription(kakaoReview.getRelativeTimeDescription())
                .photoUrl(kakaoReview.getPhotoUrl())
                .text(kakaoReview.getText())
                .build();
    }

    public NormalReviewDto buildNormalReviewDto(Reviews review, Long userId) {
        Users user = exceptionUtils.validateUser(userId);
        return NormalReviewDto.builder()
                .id(review.getId())
                .authorName(user.getUserName()) // 사용자 이름으로 설정
                .rating(review.getRating())
                .relativeTimeDescription(review.getRelativeTimeDescription())
                .photoUrl(review.getPhotoUrl())
                .text(review.getText())
                .build();
    }

    public Reviews convertToEntity(NormalReviewDto reviewDto, Users user, Optional<Restaurant> restaurant)
    {
        Reviews review = new Reviews();
        review.setAuthorName(user.getUserName()); // authorName을 로그인한 사용자의 userName으로 설정
        review.setRating(reviewDto.getRating());
        review.setRelativeTimeDescription(reviewDto.getRelativeTimeDescription());
        review.setPhotoUrl(reviewDto.getPhotoUrl());
        review.setText(reviewDto.getText());
        review.setUser(user);
        restaurant.ifPresent(review::setRestaurant);
        return review;
    }


    //utils에 옮겨야함
    private List<String> extractKoreanWords(String input) {
        Pattern pattern = Pattern.compile("[가-힣]+");
        Matcher matcher = pattern.matcher(input);
        List<String> results = new ArrayList<>();
        while (matcher.find()) {
            results.add(matcher.group());
        }
        return results;
    }
}
