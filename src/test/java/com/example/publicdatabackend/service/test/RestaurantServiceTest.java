package com.example.publicdatabackend.service.test;

import com.example.publicdatabackend.domain.restaurant.Restaurant;
import com.example.publicdatabackend.dto.restaurant.RestaurantDto;
import com.example.publicdatabackend.repository.KakaoReviewsRepository;
import com.example.publicdatabackend.repository.RestaurantRepository;
import com.example.publicdatabackend.repository.ReviewsRepository;
import com.example.publicdatabackend.repository.WishListRestaurantRepository;
import com.example.publicdatabackend.service.RestaurantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {
    @InjectMocks
    private RestaurantService target;

    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private KakaoReviewsRepository kakaoReviewsRepository;
    @Mock
    private ReviewsRepository reviewsRepository;
    @Mock
    private WishListRestaurantRepository wishListRestaurantRepository;

    private Long userId = 0L;

    @Test
    @DisplayName("매출액_역순_DTO_변환_테스트")
    public void 매출액_역순_DTO_변환_테스트() {
        // given
        doReturn(Arrays.asList(
                new Restaurant(1L, "Location1", "Address1", "123-456", "Place1", "URL1", "Desc1", "Photo1", "x1", "y1", 4.5, "S1", 50000L, 100L, "09:00-18:00"),
                new Restaurant(2L, "Location2", "Address2", "654-321", "Place2", "URL2", "Desc2", "Photo2", "x2", "y2", 3.5, "S2", 30000L, 80L, "10:00-20:00"),
                new Restaurant(3L, "Location3", "Address3", "789-123", "Place3", "URL3", "Desc3", "Photo3", "x3", "y3", 4.0, "S3", 45000L, 90L, "08:00-22:00")
        )).when(restaurantRepository).findAllByExecAmountsDesc();
        doReturn(10L).when(kakaoReviewsRepository).findKakaoReviewsNumByRestaurant(any(Restaurant.class));
        doReturn(5L).when(reviewsRepository).findReviewsNumByRestaurant(any(Restaurant.class));
        doReturn(Optional.empty()).when(wishListRestaurantRepository).findWishListRestaurantByUserIdAndRestaurantId(userId, 1L);
        doReturn(Optional.empty()).when(wishListRestaurantRepository).findWishListRestaurantByUserIdAndRestaurantId(userId, 2L);
        doReturn(Optional.empty()).when(wishListRestaurantRepository).findWishListRestaurantByUserIdAndRestaurantId(userId, 3L);

        // when
        List<RestaurantDto> results = target.getRestaurantExecAmountsDescDTO(userId);

        // then
        assertThat(results.size()).isEqualTo(3);
        assertThat(results.get(0).getReviewsNum()).isEqualTo(15L);
        assertThat(results.get(1).getReviewsNum()).isEqualTo(15L);
        assertThat(results.get(2).getReviewsNum()).isEqualTo(15L);
    }

    @Test
    @DisplayName("방문횟수_역순_DTO_변환_테스트")
    public void 방문횟수_역순_DTO_변환_테스트() {
        // given
        doReturn(Arrays.asList(
                new Restaurant(1L, "Location1", "Address1", "123-456", "Place1", "URL1", "Desc1", "Photo1", "x1", "y1", 4.5, "S1", 50000L, 100L, "09:00-18:00"),
                new Restaurant(2L, "Location2", "Address2", "654-321", "Place2", "URL2", "Desc2", "Photo2", "x2", "y2", 3.5, "S2", 30000L, 80L, "10:00-20:00"),
                new Restaurant(3L, "Location3", "Address3", "789-123", "Place3", "URL3", "Desc3", "Photo3", "x3", "y3", 4.0, "S3", 45000L, 90L, "08:00-22:00")
        )).when(restaurantRepository).findAllByNumberOfVisitDesc();
        doReturn(10L).when(kakaoReviewsRepository).findKakaoReviewsNumByRestaurant(any(Restaurant.class));
        doReturn(5L).when(reviewsRepository).findReviewsNumByRestaurant(any(Restaurant.class));
        doReturn(Optional.empty()).when(wishListRestaurantRepository).findWishListRestaurantByUserIdAndRestaurantId(userId, 1L);
        doReturn(Optional.empty()).when(wishListRestaurantRepository).findWishListRestaurantByUserIdAndRestaurantId(userId, 2L);
        doReturn(Optional.empty()).when(wishListRestaurantRepository).findWishListRestaurantByUserIdAndRestaurantId(userId, 3L);

        // when
        List<RestaurantDto> results = target.getRestaurantNumberOfVisitsDescDTO(userId);

        // then
        assertThat(results.size()).isEqualTo(3);
        assertThat(results.get(0).getReviewsNum()).isEqualTo(15L);
        assertThat(results.get(1).getReviewsNum()).isEqualTo(15L);
        assertThat(results.get(2).getReviewsNum()).isEqualTo(15L);
    }
}
