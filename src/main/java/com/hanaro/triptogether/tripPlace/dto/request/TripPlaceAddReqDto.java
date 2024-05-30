package com.hanaro.triptogether.tripPlace.dto.request;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TripPlaceAddReqDto {
    private Long tripIdx;
    private Integer tripDate;
    private Long placeIdx;
    private BigDecimal placeAmount;
    private String placeMemo;
    private Long memberIdx; //작성자 id
}