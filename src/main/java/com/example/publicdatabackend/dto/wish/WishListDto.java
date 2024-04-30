package com.example.publicdatabackend.dto.wish;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WishListDto {
    private Long userId;
    private Long restaurantId;
}
