package com.hanaro.triptogether.member.dto.request;

import lombok.Getter;

@Getter
public class LoginReqDto {
    private Long memberIdx;
    private String memberLoginPw;
}
