package com.hanaro.triptogether.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResDto {
    private String message;
    private String memberName;
}
