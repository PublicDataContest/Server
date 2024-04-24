package com.example.publicdatabackend.controller;

import com.example.publicdatabackend.dto.RestaurantDto;
import com.example.publicdatabackend.global.res.DataResponse;
import com.example.publicdatabackend.global.res.constant.ResponseMessageConstant;
import com.example.publicdatabackend.global.res.constant.StatusCodeConstant;
import com.example.publicdatabackend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("/api/execAmounts")
    public ResponseEntity<DataResponse> getExecAmountsList(@RequestParam("userId") Long userId) {
        List<RestaurantDto> response
                = restaurantService.getRestaurantExecAmountsDescDTO(userId);

        return ResponseEntity.ok()
                .body(new DataResponse(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, response));
    }
}
