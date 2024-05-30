package com.hanaro.triptogether;

import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.member.domain.MemberRepository;
import com.hanaro.triptogether.member.dto.response.LoginResDto;
import com.hanaro.triptogether.member.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class MemberServiceTests extends TriptogetherApplicationTests {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    MemberServiceImpl memberService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .memberIdx(1L)
                .memberId("testId")
                .memberPw("testPw")
                .alarmStatus(true)
                .memberLoginPw("123456")
                .memberName("memberName")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("간편 로그인 테스트- 비밀번호 맞을 때")
    void testLoginSuccess() {
        // Given
        when(memberRepository.findMemberByMemberIdxAndMemberLoginPw(1L, "123456")).thenReturn(member);

        // When
        LoginResDto result = memberService.login(1L, "123456");

        // Then
        assertEquals("로그인이 완료되었습니다!", result.getMessage());
        assertEquals(member.getMemberName(), result.getMemberName());
    }

    @Test
    @DisplayName("간편 로그인 테스트- 비밀번호 틀렸을 때")
    void testLoginIncorrectPw() {
        // Given
        when(memberRepository.findMemberByMemberIdxAndMemberLoginPw(1L, "123456")).thenReturn(member);

        // When
        LoginResDto result = memberService.login(1L, "456789");

        // Then
        assertEquals("비밀번호가 맞지 않습니다.", result.getMessage());
    }
}
