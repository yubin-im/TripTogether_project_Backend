package com.hanaro.triptogether.trip.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TripListResDto {
    private Long preferTripIdx;
    private List<TripResDto> trips;
}
