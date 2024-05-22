package com.hanaro.triptogether.exchangeRate.domain.repository;

import com.hanaro.triptogether.exchangeRate.domain.entity.ExchangeRateAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateAlarmRepository extends JpaRepository<ExchangeRateAlarm,Long> {
}
