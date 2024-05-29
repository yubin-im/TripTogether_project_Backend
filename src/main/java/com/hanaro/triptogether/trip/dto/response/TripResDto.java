package com.hanaro.triptogether.trip.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class TripResDto {
    private Long teamIdx;
    private String teamName;
    private Long tripIdx;
    private String tripName;
    private String tripContent;
    private BigDecimal tripGoalAmount;
    private Integer tripDay;
    private LocalDate tripStartDay;
    private Long countryIdx;
    private String countryNameKo;
    private String countryNameEng;
}