package com.hanaro.triptogether.teamMember.domain;

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

    @Query("SELECT count(*) FROM TeamMember tm where tm.team = :team and deletedAt is null and (tm.teamMemberState=\"총무\" or tm.teamMemberState=\"모임원\")")
    Integer findTeamMembersByTeamAndTeamMemberState(Team team);
}
