package com.example.publicdatabackend.controller;

import com.example.publicdatabackend.dto.statistics.PeopleStatisticsDto;
import com.example.publicdatabackend.dto.statistics.PriceStatisticsDto;
import com.example.publicdatabackend.dto.statistics.SeasonStatisticsDto;
import com.example.publicdatabackend.dto.statistics.TimeStatisticsDto;
import com.example.publicdatabackend.global.res.DataResponse;
import com.example.publicdatabackend.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/statistics")
public class StatisticsController {
    public final StatisticsService statisticsService;

    @GetMapping("/people/{restaurantId}")
    public ResponseEntity<DataResponse<PeopleStatisticsDto>> getPeopleStatistics(
            @PathVariable Long restaurantId
    ) {
        PeopleStatisticsDto peopleStatistics = statisticsService.getPeopleStatistics(restaurantId);
        DataResponse<PeopleStatisticsDto> response = new DataResponse<>(peopleStatistics);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/time/{restaurantId}")
    public ResponseEntity<DataResponse<TimeStatisticsDto>> getTimeStatistics(
            @PathVariable Long restaurantId
    ) {
        TimeStatisticsDto timeStatisticsDto = statisticsService.getTimeStatistics(restaurantId);
        DataResponse<TimeStatisticsDto> response = new DataResponse<>(timeStatisticsDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/season/{restaurantId}")
    public ResponseEntity<DataResponse<SeasonStatisticsDto>> getSeasonStatistics(
            @PathVariable Long restaurantId
    ) {
        SeasonStatisticsDto seasonStatisticsDto = statisticsService.getSeasonStatistics(restaurantId);
        DataResponse<SeasonStatisticsDto> response = new DataResponse<>(seasonStatisticsDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/price/{restaurantId}")
    public ResponseEntity<DataResponse<PriceStatisticsDto>> getPriceStatistics(
            @PathVariable Long restaurantId
    ) {
        PriceStatisticsDto priceStatisticsDto = statisticsService.getPriceStatistics(restaurantId);
        DataResponse<PriceStatisticsDto> response = new DataResponse<>(priceStatisticsDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
