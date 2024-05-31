package com.hanaro.triptogether.dues.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DuesListMemberResponseDto {

    private Long memberIdx;
    private String memberName;
    private BigDecimal memberAmount;
}
