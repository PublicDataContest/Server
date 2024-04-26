package com.example.publicdatabackend.utils;

import com.example.publicdatabackend.domain.restaurant.Menu;
import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.dto.map.CardDetailDto;
import com.example.publicdatabackend.dto.map.MapRestaurantDto;
import com.example.publicdatabackend.dto.menu.MenuListDto;
import com.example.publicdatabackend.dto.restaurant.RestaurantDto;
import com.example.publicdatabackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class DtoConverterUtils {
    private final KakaoReviewsRepository kakaoReviewsRepository;
    private final ReviewsRepository reviewsRepository;
    private final WishListRestaurantRepository wishListRestaurantRepository;
    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;

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
