package com.example.publicdatabackend.controller;

import com.example.publicdatabackend.dto.review.KakaoReviewDto;
import com.example.publicdatabackend.dto.review.NormalReviewDto;
import com.example.publicdatabackend.dto.review.ReviewDto;
import com.example.publicdatabackend.global.res.DataResponse;
import com.example.publicdatabackend.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/kakao/{restaurantId}")
    public ResponseEntity<List<KakaoReviewDto>> getKakaoReviewsByRestaurantId(@PathVariable Long restaurantId) {
        List<KakaoReviewDto> kakaoReviews = reviewService.getKakaoReviewsByRestaurantId(restaurantId);
        return new ResponseEntity<>(kakaoReviews, HttpStatus.OK);
    }
    @GetMapping("/normal/{userId}/{restaurantId}")
    public ResponseEntity<List<NormalReviewDto>> getNormalReviewsByUserAndRestaurant(
            @PathVariable Long userId,
            @PathVariable Long restaurantId
    ) {
        List<NormalReviewDto> normalReviews = reviewService.getNormalReviews(userId, restaurantId);
        return new ResponseEntity<>(normalReviews, HttpStatus.OK);
    }

    @GetMapping("/combine/{userId}/{restaurantId}")
    public ResponseEntity<Map<String, Object>> getReviewsByRestaurant(
            @PathVariable Long restaurantId,
            @PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();

        // 식당 ID에 따른 카카오 리뷰 가져오기
        List<KakaoReviewDto> kakaoReviews = reviewService.getKakaoReviewsByRestaurantId(restaurantId);
        response.put("kakaoReviews", kakaoReviews);

        // 식당 ID에 대한 모든 사용자의 일반 리뷰 가져오기
        List<NormalReviewDto> normalReviews = reviewService.getNormalReviews(userId, restaurantId);
        response.put("normalReviews", normalReviews);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //리뷰 등록
    @PostMapping("/normal/{userId}/{restaurantId}")
    public ResponseEntity<DataResponse<NormalReviewDto>> createNormalReview(
            @PathVariable Long restaurantId,
            @PathVariable Long userId,
            @RequestBody NormalReviewDto reviewDto) {
        NormalReviewDto createdReview = reviewService.createNormalReview(reviewDto, userId, restaurantId);
        DataResponse<NormalReviewDto> response = new DataResponse<>(createdReview);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @DeleteMapping("/normal/{userId}/{restaurantId}/{reviewId}")
    public ResponseEntity<DataResponse<Void>> deleteNormalReview(
            @PathVariable Long userId,
            @PathVariable Long restaurantId,
            @PathVariable Long reviewId
    ) {
        reviewService.deleteNormalReview(userId, restaurantId, reviewId);
        DataResponse<Void> response = new DataResponse<>(null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
