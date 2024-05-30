package com.hanaro.triptogether.tripPlace.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TripPlaceUpdateReqDto {
    private Long place_idx;
    private BigDecimal place_amount;
    private String place_memo;
    private Long member_idx; //수정자 idx
}