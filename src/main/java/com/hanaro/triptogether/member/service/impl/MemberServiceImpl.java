package com.hanaro.triptogether.member.service.impl;

import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.member.domain.MemberRepository;
import com.hanaro.triptogether.member.dto.response.LoginResDto;
import com.hanaro.triptogether.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    // 간편 로그인
    @Transactional
    @Override
    public LoginResDto login(Long memberIdx, String memberLoginPw) {
        Member member = memberRepository.findMemberByMemberIdxAndMemberLoginPw(memberIdx, memberLoginPw);

        if(member == null) {
            return LoginResDto.builder()
                    .message("비밀번호가 맞지 않습니다.")
                    .memberName("비회원")
                    .build();
        }

        if (!memberLoginPw.equals(member.getMemberLoginPw())) {
            return LoginResDto.builder()
                    .message("비밀번호가 맞지 않습니다.")
                    .memberName("비회원")
                    .build();
        } else {
            return LoginResDto.builder()
                    .message("로그인이 완료되었습니다!")
                    .memberName(member.getMemberName())
                    .build();
        }
    }

    @Override
    public Member findByMemberIdx(Long memberIdx) {
        return memberRepository.findById(memberIdx).orElseThrow(()->new ApiException(ExceptionEnum.MEMBER_NOT_FOUND));
    }

    // 알림설정 (on/off)
    @Transactional
    @Override
    public void setAlarm(Long memberIdx) {
        Member member = memberRepository.findById(memberIdx).orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_FOUND));
        Boolean alarmStatus = member.getAlarmStatus();

        if (alarmStatus == true) {
            member.updateAlarm(false);
        } else {
            member.updateAlarm(true);
        }
        memberRepository.save(member);
    }

}
