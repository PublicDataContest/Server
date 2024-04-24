package com.example.publicdatabackend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class RestaurantResponse {
    @Getter
    @ToString
    @Builder
    @AllArgsConstructor
    public static class RestaurantExecAmountsResponse {
        private Long restaurantId;
        private String placeName;
        private Long reviewsNum;
        private Boolean wishListRestaurant;
        private String currentOpeningHours;
        private String photoUrl;
    }
}
