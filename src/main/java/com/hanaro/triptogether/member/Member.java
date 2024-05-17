package com.hanaro.triptogether.member;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "Member")
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