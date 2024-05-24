package com.hanaro.triptogether.dues.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DuesListResponseDto {

    private String memberName;
    private BigDecimal memberAmount;
}
