package com.hanaro.triptogether.teamMember;

import com.hanaro.triptogether.enumeration.TeamMemberState;
import com.hanaro.triptogether.member.Member;
import com.hanaro.triptogether.team.Team;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Team_member")
@Getter
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


}
