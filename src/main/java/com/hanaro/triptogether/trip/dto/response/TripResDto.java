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
    private Integer tripDay;
    private LocalDate tripStartDay;
    private List<City> cities;
}