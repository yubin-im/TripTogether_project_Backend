package com.hanaro.triptogether.team.domain;

import com.hanaro.triptogether.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findTeamByAccount(Account account);

    // 모임서비스 전체 조회
    @Query("SELECT t FROM Team t WHERE t.teamIdx IN (SELECT tm.team.teamIdx FROM TeamMember tm WHERE tm.member.memberIdx = :memberIdx)")
    List<Team> findTeamsByMemberIdx(@Param("memberIdx") Long memberIdx);
}
