package com.example.publicdatabackend.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
public class PriceStatisticsDto {
    private Long lower10000;
    private Long lower15000;
    private Long lower20000;
    private Long upper20000;
}
