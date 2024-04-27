package com.example.publicdatabackend.controller;

import com.example.publicdatabackend.dto.menu.MenuListDto;
import com.example.publicdatabackend.global.res.DataResponse;
import com.example.publicdatabackend.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MenuDetailsController {
    private final MenuService menuService;

    @GetMapping("/menu/{userId}/{restaurantId}")
    public ResponseEntity<DataResponse<List<MenuListDto>>> getMenuDetails(
            @PathVariable Long userId,
            @PathVariable Long restaurantId
    ) {
        List<MenuListDto> menuDetails = menuService.getMenuInformation(userId, restaurantId);
        DataResponse<List<MenuListDto>> response = new DataResponse<>(menuDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
