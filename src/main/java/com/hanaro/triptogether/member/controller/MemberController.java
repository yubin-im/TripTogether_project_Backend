package com.hanaro.triptogether.member.controller;

import com.hanaro.triptogether.member.dto.request.LoginReqDto;
import com.hanaro.triptogether.member.dto.response.LoginResDto;
import com.hanaro.triptogether.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 간편 로그인
    @PostMapping("/login")
    public LoginResDto login(@RequestBody LoginReqDto loginReqDto, HttpServletRequest request) {
        Long memberIdx = loginReqDto.getMemberIdx();
        String memberLoginPw = loginReqDto.getMemberLoginPw();

        HttpSession session = request.getSession();
        session.setAttribute("loginUserIdx",loginReqDto.getMemberIdx());

        return memberService.login(memberIdx, memberLoginPw, loginReqDto.getFcmToken());
    }

    // 알림설정 (on/off)
    @PutMapping("/account/alarm")
    public void setAlarm(@RequestBody Map<String, Long> memberIdxMap) {
        Long memberIdx = memberIdxMap.get("memberIdx");
        memberService.setAlarm(memberIdx);
    }
}
