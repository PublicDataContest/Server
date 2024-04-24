package com.example.publicdatabackend.controller;

import com.example.publicdatabackend.dto.RestaurantDto;
import com.example.publicdatabackend.global.res.DataResponse;
import com.example.publicdatabackend.global.res.constant.ResponseMessageConstant;
import com.example.publicdatabackend.global.res.constant.StatusCodeConstant;
import com.example.publicdatabackend.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UsersController {
    private final UsersService usersService;

    /**
     * @Description 찜한 가게 API
     */
    @GetMapping("/{userId}/wish-restaurant")
    public ResponseEntity<DataResponse<Page<RestaurantDto>>> getWishRestaurant(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantDto> response = usersService.getWishRestaurant(userId, pageable);

        return ResponseEntity.ok().body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, response));
    }

    /**
     * @Description 리뷰 쓴 가게 API
     */
    @GetMapping("/{userId}/reviews")
    public ResponseEntity<DataResponse<Page<RestaurantDto>>> getReviews(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantDto> response = usersService.getReviews(userId, pageable);

        return ResponseEntity.ok().body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, response));
    }
}
