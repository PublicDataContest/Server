package com.example.publicdatabackend.controller;

import com.example.publicdatabackend.domain.users.WishListRestaurant;
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

    @PutMapping("/{userId}/restaurants/{restaurantId}")
    public ResponseEntity<String> toggleWish(@PathVariable Long userId, @PathVariable Long restaurantId) {
        Optional<WishListRestaurant> wish = wishListService.toggleWishList(userId, restaurantId);
        if (wish.isPresent()) {
            return ResponseEntity.ok("찜하기 성공");
        } else {
            return ResponseEntity.ok("찜하기 취소");
        }
    }
}
