package com.hanaro.triptogether.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 간편 로그인
    Member findMemberByMemberIdxAndMemberLoginPw(Long memberIdx, String memberLoginPw);
}
