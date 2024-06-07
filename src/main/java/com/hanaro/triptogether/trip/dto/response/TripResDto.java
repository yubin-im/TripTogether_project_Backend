package com.hanaro.triptogether.trip.dto.response;


import com.hanaro.triptogether.city.dto.City;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class TripResDto {
    private Long teamIdx;
    private String teamName;
    private Long tripIdx;
    private String tripName;
    private String tripContent;
    private BigDecimal tripGoalAmount;
    private BigDecimal tripExpectedAmount;
    private Integer tripDay;
    private Integer tripImg;
    private LocalDate tripStartDay;
    private Long countryIdx;
    private String countryNameKo;
    private String countryNameEng;
    private List<City> cities;
}