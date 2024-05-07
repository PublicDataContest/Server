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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/map/{userId}/{searchText}/gpt")
    public ResponseEntity<DataResponse<Page<MapRestaurantDto>>> searchRestaurantsByGpt(
            @PathVariable Long userId,
            @PathVariable String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MapRestaurantDto> restaurantDtoPage = restaurantService.viewCoordinateByGpt(userId, searchText, pageable);
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
        try {
            return weatherService.getWeatherData();
        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch weather data", e);
        }
    }

    @GetMapping("/map/{userId}/{searchText}/restaurants")
    public ResponseEntity<DataResponse<Page<MapRestaurantDto>>> searchOnlyRestaurantsByLongText(
            @PathVariable Long userId,
            @PathVariable String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MapRestaurantDto> restaurantDtoPage = restaurantService.viewCoordinateByLongText(userId, searchText, pageable);

        // 음식점만 필터링
        List<MapRestaurantDto> filteredRestaurants = restaurantDtoPage.getContent().stream()
                .filter(restaurantDto -> {
                    List<String> categories = restaurantDto.getCategory();
                    return categories != null && categories.contains("음식점");
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, new PageImpl<>(filteredRestaurants, pageable, filteredRestaurants.size())));
    }

    @GetMapping("/map/{userId}/{searchText}/non-restaurants")
    public ResponseEntity<DataResponse<Page<MapRestaurantDto>>> searchNonRestaurantsByLongText(
            @PathVariable Long userId,
            @PathVariable String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MapRestaurantDto> restaurantDtoPage = restaurantService.viewCoordinateByLongText(userId, searchText, pageable);

        // 음식점이 아닌 항목들만 필터링
        List<MapRestaurantDto> filteredNonRestaurants = restaurantDtoPage.getContent().stream()
                .filter(restaurantDto -> {
                    List<String> categories = restaurantDto.getCategory();
                    return categories == null || !categories.contains("음식점");
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, new PageImpl<>(filteredNonRestaurants, pageable, filteredNonRestaurants.size())));
    }

}
