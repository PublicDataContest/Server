package com.example.publicdatabackend.dto.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class CardDetailDto {
    private Long restaurantId;
    private String placeName;
    private Long reviewsNum;
    private Double rating;
    private Boolean wishListRestaurant;
    private String currentOpeningHours;
    private String photoUrl;
    private String addressName;
    private String phone;
}
