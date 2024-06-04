package com.hanaro.triptogether.team.domain;

import com.hanaro.triptogether.account.domain.Account;
import com.hanaro.triptogether.enumeration.PreferenceType;
import com.hanaro.triptogether.enumeration.TeamType;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.trip.domain.Trip;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "Team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamIdx;

    @ManyToOne
    @JoinColumn(name = "acc_idx", nullable = false)
    private Account account;

    @Column(nullable = false, length = 30)
    private String teamName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamType teamType;

    @Enumerated(EnumType.STRING)
    private PreferenceType preferenceType;

    private String teamNotice;


    @OneToOne
    @JoinColumn(name = "prefer_trip")
    private Trip preferTrip;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", updatable=false)
    private Member createdBy;

    private LocalDateTime lastModifiedAt;

    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    private Member lastModifiedBy;
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "deleted_by")
    private Member deletedBy;

    // 삭제 시간, 삭제한 사용자 추가
    public void delete(LocalDateTime deletedAt, Member deletedBy) {
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
    }

    // 공지 등록/수정
    public void updateTeamNotice(String teamNotice) {
        this.teamNotice = teamNotice;
    }

    public void updatePreferTrip(Trip preferTrip, Member member) {
        this.preferTrip = preferTrip;
        this.lastModifiedBy = member;
        this.lastModifiedAt=LocalDateTime.now();
    }
}
