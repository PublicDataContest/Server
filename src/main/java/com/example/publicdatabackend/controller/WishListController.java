package com.example.publicdatabackend.controller;

import com.example.publicdatabackend.domain.users.WishListRestaurant;
import com.example.publicdatabackend.dto.wish.WishListDto;
import com.example.publicdatabackend.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {
    @Autowired
    private WishListService wishListService;

    @PostMapping("/toggle")
    public ResponseEntity<Object> toggleWish(@RequestBody WishListDto createWishDTO) {
        Optional<WishListRestaurant> wish = wishListService.toggleWishList(createWishDTO.getUserId(), createWishDTO.getRestaurantId());
        if (wish.isPresent()) {
            return ResponseEntity.ok("찜하기 성공");
        } else {
            return ResponseEntity.ok("찜하기 취소");
        }
    }
}
