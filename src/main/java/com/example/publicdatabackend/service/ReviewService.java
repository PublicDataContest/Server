package com.example.publicdatabackend.service;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.domain.reviews.KakaoReviews;
import com.example.publicdatabackend.domain.reviews.Reviews;
import com.example.publicdatabackend.domain.users.Users;
import com.example.publicdatabackend.dto.review.KakaoReviewDto;
import com.example.publicdatabackend.dto.review.NormalReviewDto;
import com.example.publicdatabackend.repository.KakaoReviewsRepository;
import com.example.publicdatabackend.repository.RestaurantRepository;
import com.example.publicdatabackend.repository.ReviewsRepository;
import com.example.publicdatabackend.utils.DtoConverterUtils;
import com.example.publicdatabackend.utils.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewsRepository reviewsRepository;
    private final KakaoReviewsRepository kakaoReviewsRepository;
    private final DtoConverterUtils restaurantDtoConverterUtils;
    private final ExceptionUtils exceptionUtils;
    private final RestaurantRepository restaurantRepository;

    private Users validateUser(Long userId) {
        return exceptionUtils.validateUser(userId);
    }

    public List<NormalReviewDto> getNormalReviews(Long userId, Long restaurantId) {
        validateUser(userId);
        List<Reviews> reviews = reviewsRepository.findByUserIdAndRestaurantId(userId, restaurantId);
        return reviews.stream()
                .map(review -> restaurantDtoConverterUtils.buildNormalReviewDto(review, userId))
                .collect(Collectors.toList());
    }

    public NormalReviewDto createNormalReview(NormalReviewDto reviewDto, Long userId, Long restaurantId) {
        Users user = validateUser(userId);
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        Reviews review = restaurantDtoConverterUtils.convertToEntity(reviewDto, user, restaurant);
        Reviews savedReview = reviewsRepository.save(review);
        return restaurantDtoConverterUtils.buildNormalReviewDto(savedReview, userId);
    }
    public List<KakaoReviewDto> getKakaoReviewsByRestaurantId(Long restaurantId) {
        List<KakaoReviews> kakaoReviews = kakaoReviewsRepository.findByRestaurantId(restaurantId);
        return kakaoReviews.stream()
                .map(restaurantDtoConverterUtils::buildKakaoReviewDto)
                .collect(Collectors.toList());
    }



    public void deleteNormalReview(Long userId, Long restaurantId, Long reviewId) {
        Reviews review = (Reviews) reviewsRepository.findByUserIdAndRestaurantId(reviewId, restaurantId);
        reviewsRepository.delete(review);
    }

}
