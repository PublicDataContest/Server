package com.example.publicdatabackend.controller;

import com.example.publicdatabackend.dto.RestaurantDto;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestaurantController {
    private final RestaurantService restaurantService;

    /**
     * @Description 매출 수 API
     */
    @GetMapping("/execAmounts/{userId}")
    public ResponseEntity<DataResponse<Page<RestaurantDto>>> getExecAmountsList(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantDto> response
                = restaurantService.getRestaurantExecAmountsDescDTO(userId, pageable);

        return ResponseEntity.ok()
                .body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, response));
    }

    /**
     * @Description 방문 횟수 API
     */
    @GetMapping("/total-visit/{userId}")
    public ResponseEntity<DataResponse<Page<RestaurantDto>>> getTotalVisitsList(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantDto> response
                = restaurantService.getRestaurantNumberOfVisitDescDTO(userId, pageable);

        return ResponseEntity.ok()
                .body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, response));
    }

    /**
     * @Description 가격별 API
     */
    @GetMapping("/{price}/{userId}")
    public ResponseEntity<DataResponse<Page<RestaurantDto>>> getPriceList(
            @PathVariable(name = "userId") Long userId, @PathVariable(name = "price") Long price,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantDto> response
                = restaurantService.getRestaurantPriceDTO(userId, price, pageable);

        return ResponseEntity.ok()
                .body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, response));
    }

    /**
     * @Description 계절별 API
     */
    @GetMapping("/{userId}")
    public ResponseEntity<DataResponse<Page<RestaurantDto>>> getSeasonList(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "season") String season,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantDto> response
                = restaurantService.getRestaurantSeasonDTO(userId, season, pageable);

        return ResponseEntity.ok()
                .body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, response));
    }

    /**
     * @Description TOP5 식당 API
     */
    @GetMapping("/{userId}/top-ranking")
    public ResponseEntity<DataResponse<List<RestaurantDto>>> getTopRankingList(
            @PathVariable(name = "userId") Long userId) {
        List<RestaurantDto> response = restaurantService.getRestaurantTopRankingListDTO(userId);

        return ResponseEntity.ok()
                .body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, response));
    }
}
