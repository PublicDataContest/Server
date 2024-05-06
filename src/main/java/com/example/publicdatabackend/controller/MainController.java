package com.example.publicdatabackend.controller;

import com.example.publicdatabackend.dto.map.CardDetailDto;
import com.example.publicdatabackend.dto.map.MapRestaurantDto;
import com.example.publicdatabackend.dto.map.WeatherDataDto;
import com.example.publicdatabackend.global.res.DataResponse;
import com.example.publicdatabackend.global.res.constant.ResponseMessageConstant;
import com.example.publicdatabackend.global.res.constant.StatusCodeConstant;
import com.example.publicdatabackend.service.RestaurantService;
import com.example.publicdatabackend.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainController {
    private final RestaurantService restaurantService;
    private final WeatherService weatherService;



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

    //카드 리스트뷰를 누르면 뜨는 상세정보 API
    @GetMapping("/card/{userId}/{restaurantId}")
    public ResponseEntity<DataResponse<CardDetailDto>> getRestaurantDetails(
            @PathVariable Long userId,
            @PathVariable Long restaurantId
    ) {
        CardDetailDto cardDetailDto = restaurantService.getRestaurantDetails(userId, restaurantId);
        return ResponseEntity.ok()
                .body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, cardDetailDto));
    }



    @GetMapping("/weather")
    public List<WeatherDataDto> getWeather() {
        return weatherService.getWeatherData();
    }
}
