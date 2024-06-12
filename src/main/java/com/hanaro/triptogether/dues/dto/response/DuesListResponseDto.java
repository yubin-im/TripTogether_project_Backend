package com.hanaro.triptogether.dues.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DuesListResponseDto {

    private BigDecimal duesTotalAmount;
    private List<DuesListMemberResponseDto> memberResponseDtos;
}
