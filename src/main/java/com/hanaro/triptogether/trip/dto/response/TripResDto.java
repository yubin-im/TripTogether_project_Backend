package com.hanaro.triptogether.trip.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Builder
public class TripResDto {
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