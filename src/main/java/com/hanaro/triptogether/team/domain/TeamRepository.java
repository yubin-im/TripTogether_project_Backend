package com.hanaro.triptogether.team.domain;

import com.hanaro.triptogether.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findTeamByAccount(Account account);
}
