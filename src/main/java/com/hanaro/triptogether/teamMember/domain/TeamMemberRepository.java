package com.hanaro.triptogether.teamMember.domain;

import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember,Long> {

    @Query("SELECT tm FROM TeamMember tm WHERE tm.team.teamIdx = :teamIdx")
    List<TeamMember> findAllByTeamIdx(@Param("teamIdx") Long teamIdx);

    List<TeamMember> findTeamMembersByTeam(Team team);

    List<TeamMember> findTeamMemberByMember_MemberIdx(Long memberIdx);

    Optional<TeamMember> findTeamMemberByMember_MemberIdxAndTeam_TeamIdx(Long memberIdx, Long teamIdx);

    TeamMember findTeamMemberByMemberAndTeam(Member member, Team team);
}
