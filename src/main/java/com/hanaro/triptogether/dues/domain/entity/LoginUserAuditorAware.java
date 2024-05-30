package com.hanaro.triptogether.dues.domain.entity;

import com.hanaro.triptogether.member.domain.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginUserAuditorAware implements AuditorAware<Long> {

    private final HttpSession httpSession;


    @Override
    public Optional<Long> getCurrentAuditor() {
        Member member = (Member)httpSession.getAttribute("loginUserIdx");
        return Optional.ofNullable(1L);
    }
}
