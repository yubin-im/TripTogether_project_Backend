package com.hanaro.triptogether.teamMember.domain;

import com.hanaro.triptogether.enumeration.TeamMemberState;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.team.domain.Team;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Team_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamMemberIdx;

    @ManyToOne
    @JoinColumn(name = "team_idx", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "member_idx", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamMemberState teamMemberState = TeamMemberState.요청중;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;
    private Long lastModifiedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;

    // 상태 변경
    public void updateTeamMemberState(TeamMemberState teamMemberState) {
        this.teamMemberState = teamMemberState;
    }
}
