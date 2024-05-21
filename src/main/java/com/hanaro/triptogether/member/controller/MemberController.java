package com.hanaro.triptogether.member.controller;

import com.hanaro.triptogether.member.dto.request.LoginReqDto;
import com.hanaro.triptogether.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 간편 로그인
    @PostMapping("/login")
    public String login(@RequestBody LoginReqDto loginReqDto) {
        Long memberIdx = loginReqDto.getMemberIdx();
        String memberLoginPw = loginReqDto.getMemberLoginPw();

        String result = memberService.login(memberIdx, memberLoginPw);

        return result;
    }

}
