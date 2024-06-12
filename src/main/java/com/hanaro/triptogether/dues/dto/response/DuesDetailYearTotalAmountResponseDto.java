package com.hanaro.triptogether.dues.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DuesDetailYearTotalAmountResponseDto {
    private BigDecimal duesAmount;
    private int duesOfMonth;

}
