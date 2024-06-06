package com.hanaro.triptogether.exchangeRate.domain.repository;

import com.hanaro.triptogether.exchangeRate.domain.entity.ExchangeRateAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeRateAlarmRepository extends JpaRepository<ExchangeRateAlarm,Long> {

    List<ExchangeRateAlarm> findExchangeRateAlarmByMember_MemberIdx(Long memberIdx);
}
