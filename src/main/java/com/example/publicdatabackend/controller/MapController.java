package com.example.publicdatabackend.controller;

import com.example.publicdatabackend.dto.restaurant.RestaurantDto;
import com.example.publicdatabackend.global.res.DataResponse;
import com.example.publicdatabackend.global.res.constant.ResponseMessageConstant;
import com.example.publicdatabackend.global.res.constant.StatusCodeConstant;
import com.example.publicdatabackend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MapController {
    private final RestaurantService restaurantService;

    // 기존의 메서드들은 그대로 유지됩니다.

    // longText를 기준으로 식당 검색하는 엔드포인트
    @GetMapping("/map/{userId}/{searchText}")
    public ResponseEntity<DataResponse<Page<RestaurantDto>>> searchRestaurantsByLongText(
            @PathVariable Long userId,
            @PathVariable String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantDto> restaurantDtoPage = restaurantService.searchRestaurantsByLongText(userId, searchText, pageable);
        return ResponseEntity.ok()
                .body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, restaurantDtoPage));

    }
}
