package com.hanaro.triptogether.dues.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DuesDetailOfMonthAmountRequestDto {
    private Long accIdx;
    private Long memberIdx;
    private String duesYear;
}
