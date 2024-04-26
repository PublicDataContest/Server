package com.example.publicdatabackend.utils;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.dto.map.MapRestaurantDto;
import com.example.publicdatabackend.dto.restaurant.RestaurantDto;
import com.example.publicdatabackend.repository.CategoryRepository;
import com.example.publicdatabackend.repository.KakaoReviewsRepository;
import com.example.publicdatabackend.repository.ReviewsRepository;
import com.example.publicdatabackend.repository.WishListRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class RestaurantDtoConverterUtils {
    private final KakaoReviewsRepository kakaoReviewsRepository;
    private final ReviewsRepository reviewsRepository;
    private final WishListRestaurantRepository wishListRestaurantRepository;

    private final CategoryRepository categoryRepository;

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
                .rating(restaurant.getRating())
                .x(restaurant.getX())
                .y(restaurant.getY())
                .wishListRestaurant(wishListRestaurant)
                .categoryName(categoryName)  // categoryName 추가
                .hashTags(hashTags)          // hashTags 추가
                .category(extractedWords)
                .build();
    }
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
