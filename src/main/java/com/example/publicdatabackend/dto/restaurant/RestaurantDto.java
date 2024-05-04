package com.example.publicdatabackend.dto.restaurant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantDto {
    private Long restaurantId;
    private String placeName;
    private Long reviewsNum;
    private Double rating;
    private Boolean wishListRestaurant;
    private String currentOpeningHours;
    private String photoUrl;
    private String hashTags;
    private Boolean priceModel;
}
