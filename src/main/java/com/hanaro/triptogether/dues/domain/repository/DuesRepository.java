package com.hanaro.triptogether.dues.domain.repository;

import com.hanaro.triptogether.dues.domain.entity.Dues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DuesRepository extends JpaRepository<Dues,Long> {

    Dues findDuesByTeamIdx(Long teamIdx);
}
