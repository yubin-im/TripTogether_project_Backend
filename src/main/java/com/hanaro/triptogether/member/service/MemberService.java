package com.hanaro.triptogether.member.service;

import com.hanaro.triptogether.member.domain.Member;

public interface MemberService {
    // 간편 로그인
    String login(Long memberIdx, String memberLoginPw);
    //멤머 아이디로 멤버 검색
    Member findByMemberId(String memberId);
    // 알림설정 (on/off)
    void setAlarm(Long memberIdx);
}
