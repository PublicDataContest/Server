package com.example.publicdatabackend.controller;

import com.example.publicdatabackend.dto.restaurant.RestaurantDto;
import com.example.publicdatabackend.dto.restaurant.Top5RankingDto;
import com.example.publicdatabackend.exception.GlobalExceptionHandler;
import com.example.publicdatabackend.global.res.DataResponse;
import com.example.publicdatabackend.global.res.constant.ResponseMessageConstant;
import com.example.publicdatabackend.global.res.constant.StatusCodeConstant;
import com.example.publicdatabackend.service.RestaurantService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "리스트 API", description = "Response List API")
public class RestaurantController {
    private final RestaurantService restaurantService;

    /**
     * @Description 매출 수 API
     */
    @GetMapping("/execAmounts/{userId}")
    @Operation(summary = "매출 수 List API", description = "매출 수 List API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "400", description = "UserId Not Found Exception", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "회원 PK 값", example = "1"),
            @Parameter(name = "page", description = "페이지 처리 페이지 수", example = "0"),
            @Parameter(name = "size", description = "페이지 당 응답 받을 데이터 개수", example = "10"),
    })
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
     * @Description 평점 순 API
     */
    @GetMapping("/ratings/{userId}")
    @Operation(summary = "평점 순 List API", description = "매출 수 List API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "400", description = "UserId Not Found Exception", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "회원 PK 값", example = "1"),
            @Parameter(name = "page", description = "페이지 처리 페이지 수", example = "0"),
            @Parameter(name = "size", description = "페이지 당 응답 받을 데이터 개수", example = "10"),
    })
    public ResponseEntity<DataResponse<Page<RestaurantDto>>> getRatingsList(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<RestaurantDto> response = restaurantService.getRatingsListDTO(userId, pageable);

        return ResponseEntity.ok()
                .body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, response));
    }

    /**
     * @Description 방문 횟수 API
     */
    @GetMapping("/total-visit/{userId}")
    @Operation(summary = "방문 횟수 List API", description = "매출 수 List API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "400", description = "UserId Not Found Exception", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "회원 PK 값", example = "1"),
            @Parameter(name = "page", description = "페이지 처리 페이지 수", example = "0"),
            @Parameter(name = "size", description = "페이지 당 응답 받을 데이터 개수", example = "10"),
    })
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
    @Operation(summary = "가격별 List API", description = "매출 수 List API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "400", description = "UserId Not Found Exception", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "회원 PK 값", example = "1"),
            @Parameter(name = "price", description = "가격대 (Long Type) (price ≤ 10000 → 10000이하 price ≤ 15000→ 15000이하 price ≤ 20000 → 20000이하 price > 20000 → 20000이상)", example = "10000"),
            @Parameter(name = "page", description = "페이지 처리 페이지 수", example = "0"),
            @Parameter(name = "size", description = "페이지 당 응답 받을 데이터 개수", example = "10"),
    })
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
    @Operation(summary = "계절별 List API", description = "매출 수 List API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "400", description = "UserId Not Found Exception", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Not Allowed Season Type", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "회원 PK 값", example = "1"),
            @Parameter(name = "season", description = "계절 입력 (spring, summer, fall, winter만 가능)", example = "spring"),
            @Parameter(name = "page", description = "페이지 처리 페이지 수", example = "0"),
            @Parameter(name = "size", description = "페이지 당 응답 받을 데이터 개수", example = "10"),
    })
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
     * @Description 시간대별 API
     */
    @GetMapping("/time/{userId}")
    @Operation(summary = "시간대별 List API", description = "시간대별 List API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "400", description = "UserId Not Found Exception", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Not Allowed Time Type", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "회원 PK 값", example = "1"),
            @Parameter(name = "time", description = "시간대 입력 (morning, lunch, dinner만 가능)", example = "morning"),
            @Parameter(name = "page", description = "페이지 처리 페이지 수", example = "0"),
            @Parameter(name = "size", description = "페이지 당 응답 받을 데이터 개수", example = "10"),
    })
    public ResponseEntity<DataResponse<Page<RestaurantDto>>> getTimeList(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "time") String time,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantDto> response = restaurantService.getRestaurantTimeDTO(userId, time, pageable);

        return ResponseEntity.ok()
                .body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, response));
    }

    @GetMapping("/people/{userId}/{people}")
    @Operation(summary = "인원별 List API", description = "인원별 List API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "400", description = "UserId Not Found Exception", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "회원 PK 값", example = "1"),
            @Parameter(name = "people", description = "인원 수 입력", example = "5"),
            @Parameter(name = "page", description = "페이지 처리 페이지 수", example = "0"),
            @Parameter(name = "size", description = "페이지 당 응답 받을 데이터 개수", example = "10"),
    })
    public ResponseEntity<DataResponse<Page<RestaurantDto>>> getPeopleList(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "people") Long people,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantDto> response = restaurantService.getRestaurantPeopleDTO(userId, people, pageable);

        return ResponseEntity.ok()
                .body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, response));
    }

    /**
     * @Description TOP5 식당 API
     */
    @GetMapping("/{userId}/top-ranking")
    @Operation(summary = "TOP5 식당 API", description = "TOP5 식당 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "400", description = "UserId Not Found Exception", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "회원 PK 값", example = "1"),
            @Parameter(name = "page", description = "페이지 처리 페이지 수", example = "0"),
            @Parameter(name = "size", description = "페이지 당 응답 받을 데이터 개수", example = "10"),
    })
    public ResponseEntity<DataResponse<List<Top5RankingDto>>> getTopRankingList(
            @PathVariable(name = "userId") Long userId) {
        List<Top5RankingDto> response = restaurantService.getRestaurantTopRankingListDTO(userId);

        return ResponseEntity.ok()
                .body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, response));
    }

    /**
     * @Description 나를 위한 맞춤 맛집 API
     */
    @GetMapping("/recommendation/{userId}")
    @Operation(summary = "나를 위한 맞춤 맛집 API", description = "나를 위한 맞춤 맛집 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "400", description = "UserId Not Found Exception", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "회원 PK 값", example = "1"),
            @Parameter(name = "longText", description = "구 정보", example = "용산구"),
            @Parameter(name = "page", description = "페이지 처리 페이지 수", example = "0"),
            @Parameter(name = "size", description = "페이지 당 응답 받을 데이터 개수", example = "10"),
    })
    public ResponseEntity<DataResponse<Page<RestaurantDto>>> getCustomeRestaurantList(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "longText") String longText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantDto> response = restaurantService.getRecommendationRestaurantDTO(userId, longText, pageable);

        return ResponseEntity.ok()
                .body(new DataResponse<>(StatusCodeConstant.OK_STATUS_CODE, ResponseMessageConstant.SUCCESS, response));
    }
}
