package com.hanaro.triptogether.member.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberIdx;

    @Column(nullable = false, length = 30)
    private String memberId;

    @Column(nullable = false, length = 50)
    private String memberPw;

    @Column(nullable = false)
    private Boolean alarmStatus = true;

    @Column(length = 6)
    private String memberLoginPw;

    @Column(nullable = false, length = 20)
    private String memberName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;
}