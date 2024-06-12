package com.hanaro.triptogether.dues.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class DuesRuleResponseDto {

    private String duesDate;
    private BigDecimal duesAmount;

}
