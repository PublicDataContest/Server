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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("/api/execAmounts/{userId}")
    public ResponseEntity<DataResponse<Page<RestaurantDto.RestaurantExecAmounts>>> getExecAmountsList(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantDto.RestaurantExecAmounts> response
                = restaurantService.getRestaurantExecAmountsDescDTO(userId, pageable);

        return ResponseEntity.ok()
                .body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, response));
    }
}
