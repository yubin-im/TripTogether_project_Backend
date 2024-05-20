package com.hanaro.triptogether.member.service.impl;

import com.hanaro.triptogether.member.domain.MemberRepository;
import com.hanaro.triptogether.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

}
