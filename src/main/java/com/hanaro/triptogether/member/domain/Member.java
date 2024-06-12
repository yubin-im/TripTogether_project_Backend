package com.hanaro.triptogether.member.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
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

    @Column
    private String fcmToken;

    @Column(nullable = false)
    private Boolean alarmStatus = true;

    @Column(length = 6)
    private String memberLoginPw;

    @Column(nullable = false, length = 20)
    private String memberName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    // 알림설정 (on/off)
    public void updateAlarm(Boolean alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}