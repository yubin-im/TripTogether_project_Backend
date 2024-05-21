package com.hanaro.triptogether.account.domain;

import com.hanaro.triptogether.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // 모임서비스 전체 조회
    List<Account> findAccountsByMember(Member member);

}
