package com.hanaro.triptogether.exchangeRate.domain.repository;

import com.hanaro.triptogether.exchangeRate.domain.entity.ExchangeRateAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateAlarmRepository extends JpaRepository<ExchangeRateAlarm,Long> {

    List<ExchangeRateAlarm> findExchangeRatesAlarmByMember_MemberIdx(Long memberIdx);

    ExchangeRateAlarm findExchangeRateAlarmByMember_MemberIdx(Long memberIdx);

    Optional<ExchangeRateAlarm> findByMember_MemberIdxAndAlarmIdx(Long memberIdx, Long alarmIdx);

}
