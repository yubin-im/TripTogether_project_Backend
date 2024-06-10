package com.hanaro.triptogether.exchangeRate.domain.repository;

import com.hanaro.triptogether.exchangeRate.domain.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate,Long> {

    ExchangeRate findExchangeRateByCurCode(String curCode);
}
