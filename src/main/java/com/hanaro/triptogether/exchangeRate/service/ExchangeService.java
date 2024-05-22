package com.hanaro.triptogether.exchangeRate.service;

import com.hanaro.triptogether.common.BigDecimalConverter;
import com.hanaro.triptogether.common.firebase.FirebaseFCMService;
import com.hanaro.triptogether.enumeration.ExchangeRateAlarmType;
import com.hanaro.triptogether.exchangeRate.domain.entity.ExchangeRate;
import com.hanaro.triptogether.exchangeRate.domain.entity.ExchangeRateAlarm;
import com.hanaro.triptogether.exchangeRate.domain.repository.ExchangeRateAlarmRepository;
import com.hanaro.triptogether.exchangeRate.domain.repository.ExchangeRateRepository;
import com.hanaro.triptogether.exchangeRate.dto.ExchangeDto;
import com.hanaro.triptogether.exchangeRate.dto.request.ExchangeRateAlarmRequestDto;
import com.hanaro.triptogether.exchangeRate.dto.request.ExchangeRateResponseDto;
import com.hanaro.triptogether.exchangeRate.dto.request.FcmSendDto;
import com.hanaro.triptogether.exchangeRate.exception.EntityNotFoundException;
import com.hanaro.triptogether.exchangeRate.utils.ExchangeUtils;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.domain.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final ExchangeRateAlarmRepository exchangeRateAlarmRepository;
    private final TeamRepository teamRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    private final ExchangeUtils exchangeUtils;
    private FirebaseFCMService firebaseFCMService;

    public List<ExchangeRateResponseDto> getExchangeRate(){

        List<ExchangeDto> exchangeDtoList = exchangeUtils.getExchangeDataAsDtoList();
        List<ExchangeRateResponseDto> exchangeRateResponseDtos = new ArrayList<>();
        for(ExchangeDto exchangeDto: exchangeDtoList){
            exchangeRateResponseDtos.add(exchangeDto.toDto());

        }
        return  exchangeRateResponseDtos;
    }

    @Transactional
    public void saveExchangeRate(String curCode, String curRate) {
        ExchangeRate existingExchangeRate = exchangeRateRepository.findExchangeRateByCurCd(curCode);

        if (existingExchangeRate != null) {
            existingExchangeRate.updateExchangeRate(BigDecimalConverter.convertStringToBigDecimal(curRate));
            exchangeRateRepository.save(existingExchangeRate);

        }else {

            exchangeRateRepository.save(ExchangeRate.builder().rate(BigDecimalConverter.convertStringToBigDecimal(curRate)).curCd(curCode).build());
        }
    }

    @Transactional
    public void setExchangeRateAlarm(ExchangeRateAlarmRequestDto requestDto){
        Team team = teamRepository.findById(requestDto.getTeamIdx()).orElseThrow(EntityNotFoundException::new);
        ExchangeRate exchangeRate = exchangeRateRepository.findExchangeRateByCurCd(requestDto.getCurCode());
        exchangeRateAlarmRepository.save(requestDto.toEntity(team,exchangeRate));
    }

    @Transactional
    public void checkNotifyAlarms() throws IOException {

        List<ExchangeRateAlarm> alarms = exchangeRateAlarmRepository.findAll();
        //List<ExchangeRate> currRates = exchangeRateRepository.findAll();

        for(ExchangeRateAlarm alarm : alarms){
            if(alarm.getNotified()) {
                continue;
            }
            ExchangeRate exchangeRate = exchangeRateRepository.findExchangeRateByCurCd(alarm.getCurCode());
            BigDecimal currRate = exchangeRate.getRate();
            if(alarm.getCurCode().equals(exchangeRate.getCurCd())){
                boolean notify = false;
                if (alarm.getRateType() == ExchangeRateAlarmType.OVER && currRate.compareTo(alarm.getCurRate()) >= 0) {
                    notify = true;
                } else if (alarm.getRateType() == ExchangeRateAlarmType.LESS && currRate.compareTo(alarm.getCurRate()) <= 0) {
                    notify = true;
                }

                if (notify) {
                    firebaseFCMService.sendMessageTo(FcmSendDto.builder().token(alarm.getFcmToken()).title("환율 알림").body("설정한 환율 조건에 도달했습니다.").build());
                    alarm.setNotified(true);
                    exchangeRateAlarmRepository.save(alarm);

                }
            }

        }

        System.out.println("ssssaaaa"+alarms.size());
    }

    @Transactional
    public void resetNotifiedFlags() {
        List<ExchangeRateAlarm> alarms = exchangeRateAlarmRepository.findAll();
        for (ExchangeRateAlarm alarm : alarms) {
            alarm.setNotified(false);
            exchangeRateAlarmRepository.save(alarm);
        }
    }

}
