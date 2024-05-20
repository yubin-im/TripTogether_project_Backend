package com.hanaro.triptogether.member.service;

public interface MemberService {
    // 간편 로그인
    String login(Long memberIdx, String memberLoginPw);
}
