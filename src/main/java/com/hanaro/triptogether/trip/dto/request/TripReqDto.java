package com.hanaro.triptogether.trip.dto.request;


import com.hanaro.triptogether.city.dto.City;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.trip.domain.Trip;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class TripReqDto {
    private Long teamIdx;
    private String teamName;
    private Long tripIdx;
    private String tripName;
    private String tripContent;
    private BigDecimal tripGoalAmount;
    private Integer tripDay;
    private Integer tripImg;
    private LocalDate tripStartDay;
    private List<Long> cities;
    private Long createdBy;
}