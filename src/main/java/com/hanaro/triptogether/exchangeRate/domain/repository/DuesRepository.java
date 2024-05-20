package com.hanaro.triptogether.exchangeRate.domain.repository;

import com.hanaro.triptogether.dues.Dues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DuesRepository extends JpaRepository<Long, Dues> {


}
