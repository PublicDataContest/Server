package com.example.publicdatabackend.controller;

import com.example.publicdatabackend.dto.map.MapRestaurantDto;
import com.example.publicdatabackend.dto.restaurant.RestaurantDto;
import com.example.publicdatabackend.global.res.DataResponse;
import com.example.publicdatabackend.global.res.constant.ResponseMessageConstant;
import com.example.publicdatabackend.global.res.constant.StatusCodeConstant;
import com.example.publicdatabackend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
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


    //구 정보를 기준으로 카드 리스트를 알려주는 api
    @GetMapping("/map/{userId}/{searchText}")
    public ResponseEntity<DataResponse<Page<MapRestaurantDto>>> searchRestaurantsByLongText(
            @PathVariable Long userId,
            @PathVariable String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MapRestaurantDto> restaurantDtoPage = restaurantService.viewCoordinateByLongText(userId, searchText, pageable);
        return ResponseEntity.ok()
                .body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, restaurantDtoPage));

    }

    //카드 리스트뷰를 누르면 뜨는 상세정보 api
}
