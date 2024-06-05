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
import com.hanaro.triptogether.exchangeRate.dto.request.ExchangeRateInfoResponseDto;
import com.hanaro.triptogether.exchangeRate.dto.request.ExchangeRateResponse;
import com.hanaro.triptogether.exchangeRate.dto.request.FcmSendDto;
import com.hanaro.triptogether.exchangeRate.exception.EntityNotFoundException;
import com.hanaro.triptogether.exchangeRate.utils.ExchangeUtils;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.member.domain.MemberRepository;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.domain.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final ExchangeRateAlarmRepository exchangeRateAlarmRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    private final ExchangeUtils exchangeUtils;
    private FirebaseFCMService firebaseFCMService;

    public ExchangeRateInfoResponseDto getExchangeRate(){

        List<ExchangeDto> exchangeDtoList = exchangeUtils.getExchangeDataAsDtoList();
        List<ExchangeRateResponse> exchangeRateResponseDtos = new ArrayList<>();
        for(ExchangeDto exchangeDto: exchangeDtoList){
            ExchangeRate exchangeRate = exchangeRateRepository.findExchangeRateByCurCd(exchangeDto.getCur_unit());
            exchangeRateResponseDtos.add(exchangeDto.toDto(exchangeRate.getCurIcon()));

        }
        return ExchangeRateInfoResponseDto.builder().exchangeRateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).exchangeRates(exchangeRateResponseDtos).build();
    }

    @Transactional
    public void saveExchangeRate(String curCode, String curRate,String curName) {
        ExchangeRate existingExchangeRate = exchangeRateRepository.findExchangeRateByCurCd(curCode);

        if (existingExchangeRate != null) {
            existingExchangeRate.updateExchangeRate(BigDecimalConverter.convertStringToBigDecimal(curRate));
            exchangeRateRepository.save(existingExchangeRate);

        }else {

            exchangeRateRepository.save(ExchangeRate.builder().rate(BigDecimalConverter.convertStringToBigDecimal(curRate)).curCd(curCode).curName(curName).build());
        }
    }

    @Transactional
    public void setExchangeRateAlarm(ExchangeRateAlarmRequestDto requestDto){
        Member member = memberRepository.findById(requestDto.getMemberIdx()).orElseThrow(EntityNotFoundException::new);
        ExchangeRate exchangeRate = exchangeRateRepository.findExchangeRateByCurCd(requestDto.getCurCode());
        exchangeRateAlarmRepository.save(requestDto.toEntity(member,exchangeRate));
    }

    @Transactional
    public void checkNotifyAlarms() throws IOException {

        List<ExchangeRateAlarm> alarms = exchangeRateAlarmRepository.findAll();

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
