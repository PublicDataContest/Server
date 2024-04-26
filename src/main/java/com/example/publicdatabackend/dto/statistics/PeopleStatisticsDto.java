package com.example.publicdatabackend.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
public class PeopleStatisticsDto {
    private Long lower5;
    private Long lower10;
    private Long lower20;
    private Long upper20;

}
