package com.example.publicdatabackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class RestaurantDto {
    @Getter
    @ToString
    @Builder
    @AllArgsConstructor
    public static class RestaurantExecAmounts {
        private Long restaurantId;
        private String placeName;
        private Long reviewsNum;
        private Double rating;
        private Boolean wishListRestaurant;
        private String currentOpeningHours;
        private String photoUrl;
    }
}
