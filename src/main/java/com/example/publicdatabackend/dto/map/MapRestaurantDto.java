package com.example.publicdatabackend.dto.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class MapRestaurantDto {
    private Long restaurantId;
    private String placeName;
    private Long reviewsNum;
    private Double rating;
    private Boolean wishListRestaurant;
    private String photoUrl;

    private String x;
    private String y;

    private String hashTags;
    private String categoryName;
    private List<String> category;//리스트로 만든 해시태그


}
