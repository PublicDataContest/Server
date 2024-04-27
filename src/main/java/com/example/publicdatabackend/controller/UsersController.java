package com.example.publicdatabackend.controller;

import com.example.publicdatabackend.dto.restaurant.RestaurantDto;
import com.example.publicdatabackend.exception.GlobalExceptionHandler;
import com.example.publicdatabackend.global.res.DataResponse;
import com.example.publicdatabackend.global.res.constant.ResponseMessageConstant;
import com.example.publicdatabackend.global.res.constant.StatusCodeConstant;
import com.example.publicdatabackend.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "내정보 API", description = "Response List API")
public class UsersController {
    private final UsersService usersService;

    /**
     * @Description 찜한 가게 API
     */
    @GetMapping("/{userId}/wish-restaurant")
    @Operation(summary = "찜한 가게 API", description = "찜한 가게 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "400", description = "UserId Not Found Exception", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "회원 PK 값", example = "1"),
            @Parameter(name = "page", description = "페이지 처리 페이지 수", example = "0"),
            @Parameter(name = "size", description = "페이지 당 응답 받을 데이터 개수", example = "10"),
    })
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
    @Operation(summary = "리뷰 쓴 가게 API", description = "리뷰 쓴 가게 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "400", description = "UserId Not Found Exception", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "회원 PK 값", example = "1"),
            @Parameter(name = "page", description = "페이지 처리 페이지 수", example = "0"),
            @Parameter(name = "size", description = "페이지 당 응답 받을 데이터 개수", example = "10"),
    })
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
