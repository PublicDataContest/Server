package com.example.publicdatabackend.service;

import com.example.publicdatabackend.aws.S3Service;
import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.domain.reviews.KakaoReviews;
import com.example.publicdatabackend.domain.reviews.Reviews;
import com.example.publicdatabackend.domain.users.Users;
import com.example.publicdatabackend.dto.review.KakaoReviewDto;
import com.example.publicdatabackend.dto.review.NormalReviewDto;
import com.example.publicdatabackend.repository.*;
import com.example.publicdatabackend.utils.DtoConverterUtils;
import com.example.publicdatabackend.utils.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewsRepository reviewsRepository;
    private final KakaoReviewsRepository kakaoReviewsRepository;
    private final DtoConverterUtils restaurantDtoConverterUtils;
    private final ExceptionUtils exceptionUtils;
    private final RestaurantRepository restaurantRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;


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

    @Transactional
    public NormalReviewDto createNormalReview(NormalReviewDto reviewDto, Long userId, Long restaurantId) throws IOException {
        Users user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        if (reviewDto.getPhotoFile() != null && !reviewDto.getPhotoFile().isEmpty()) {
            String photoUrl = s3Service.uploadMultipartFile(reviewDto.getPhotoFile());
            reviewDto.setPhotoUrl(photoUrl);
        }

        Reviews review = new Reviews();
        review.setAuthorName(user.getUserName()); // 사용자 이름 자동 설정
        review.setRating(reviewDto.getRating());
        review.setRelativeTimeDescription(reviewDto.getRelativeTimeDescription());
        review.setPhotoUrl(reviewDto.getPhotoUrl());
        review.setText(reviewDto.getText());
        review.setUser(user);
        review.setRestaurant(restaurant);

        Reviews savedReview = reviewsRepository.save(review);
        reviewDto.setAuthorName(user.getUserName()); // DTO에도 사용자 이름 설정
        reviewDto.setId(savedReview.getId()); // 저장된 리뷰의 ID를 DTO에 설정
        return reviewDto;
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
