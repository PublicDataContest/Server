package com.example.publicdatabackend.service;

import com.example.publicdatabackend.domain.statistics.CostsStatistics;
import com.example.publicdatabackend.domain.statistics.PeopleStatistics;
import com.example.publicdatabackend.domain.statistics.SeasonsStatistics;
import com.example.publicdatabackend.domain.statistics.TimeStatistics;
import com.example.publicdatabackend.dto.statistics.PeopleStatisticsDto;
import com.example.publicdatabackend.dto.statistics.PriceStatisticsDto;
import com.example.publicdatabackend.dto.statistics.SeasonStatisticsDto;
import com.example.publicdatabackend.dto.statistics.TimeStatisticsDto;
import com.example.publicdatabackend.repository.*;
import com.example.publicdatabackend.utils.DtoConverterUtils;
import com.example.publicdatabackend.utils.ExceptionUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StatisticsService {
    private final RestaurantRepository restaurantRepository;
    private final CostsStatisticsRepository costsStatisticsRepository;
    private final SeasonStatisticsRepository seasonsRepository;
    private final TimeStatisticsRepository timeStatisticsRepository;
    private final PeopleStatisticsRepository peopleStatisticsRepository;
    private final DtoConverterUtils restaurantDtoConverterUtils;
    private final ExceptionUtils exceptionUtils;

    public PeopleStatisticsDto getPeopleStatistics(Long restaurantId){
        Optional<PeopleStatistics> peopleStatistics = peopleStatisticsRepository.findById(restaurantId);
        return restaurantDtoConverterUtils.buildPeopleDto(peopleStatistics);
    }

    public TimeStatisticsDto getTimeStatistics(Long restaurantId){
        Optional<TimeStatistics> timeStatistics = timeStatisticsRepository.findById(restaurantId);
        return restaurantDtoConverterUtils.buildTimeDto(timeStatistics);
    }

    public SeasonStatisticsDto getSeasonStatistics(Long restaurantId){
        Optional<SeasonsStatistics> seasonsStatistics = seasonsRepository.findById(restaurantId);
        return restaurantDtoConverterUtils.buildSeasonDto(seasonsStatistics);
    }

    public PriceStatisticsDto getPriceStatistics(Long restaurantId){
        Optional<CostsStatistics> costsStatistics = costsStatisticsRepository.findById(restaurantId);
        return restaurantDtoConverterUtils.buildCostDto(costsStatistics);
    }
}
