package com.hanaro.triptogether.exchangeRate.service;

import com.hanaro.triptogether.common.BigDecimalConverter;
import com.hanaro.triptogether.common.firebase.FirebaseFCMService;
import com.hanaro.triptogether.common.response.BaseResponse;
import com.hanaro.triptogether.enumeration.ExchangeRateAlarmType;
import com.hanaro.triptogether.exchangeRate.domain.entity.ExchangeRate;
import com.hanaro.triptogether.exchangeRate.domain.entity.ExchangeRateAlarm;
import com.hanaro.triptogether.exchangeRate.domain.repository.ExchangeRateAlarmRepository;
import com.hanaro.triptogether.exchangeRate.domain.repository.ExchangeRateRepository;
import com.hanaro.triptogether.exchangeRate.dto.request.ExchangeRateAlarmRequestDto;
import com.hanaro.triptogether.exchangeRate.dto.request.ExchangeRateInfoResponseDto;
import com.hanaro.triptogether.exchangeRate.dto.request.ExchangeRateResponse;
import com.hanaro.triptogether.exchangeRate.dto.request.FcmSendDto;
import com.hanaro.triptogether.exchangeRate.dto.response.ExchangeRateAlarmResponseDto;
import com.hanaro.triptogether.exchangeRate.exception.AlarmNotFoundException;
import com.hanaro.triptogether.exchangeRate.exception.EntityNotFoundException;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.member.domain.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    private final FirebaseFCMService firebaseFCMService;

    public ExchangeRateInfoResponseDto getExchangeRate(){

        List<ExchangeRate> exchangeRateDtoList = exchangeRateRepository.findAll();
        List<ExchangeRateResponse> exchangeRateResponses = new ArrayList<>();
        for(ExchangeRate exchangeDto: exchangeRateDtoList){
            ExchangeRate exchangeRate = exchangeRateRepository.findExchangeRateByCurCode(exchangeDto.getCurCode());
            exchangeRateResponses.add(exchangeDto.toDto(exchangeRate.getCurIcon()));

        }
        return ExchangeRateInfoResponseDto.builder().exchangeRateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).exchangeRates(exchangeRateResponses).build();
    }

    public List<ExchangeRateAlarmResponseDto> getExchangeRateAlarmList(Long memberIdx) {

        return exchangeRateAlarmRepository.findExchangeRatesAlarmByMember_MemberIdx(memberIdx).stream().map(entity -> {
            ExchangeRate exchangeRate = exchangeRateRepository.findById(entity.getExchangeRate().getCurIdx()).orElseThrow(EntityNotFoundException::new);
            return entity.toDto(entity,exchangeRate);
        }).toList();
    }

    @Transactional
    public void saveExchangeRate(String curCode, String curRate,String curName) {
        ExchangeRate existingExchangeRate = exchangeRateRepository.findExchangeRateByCurCode(curCode);

        if (existingExchangeRate != null) {
            existingExchangeRate.updateExchangeRate(BigDecimalConverter.convertStringToBigDecimal(curRate));
            exchangeRateRepository.save(existingExchangeRate);

        }else {

            exchangeRateRepository.save(ExchangeRate.builder().curRate(BigDecimalConverter.convertStringToBigDecimal(curRate)).curCode(curCode).curName(curName).build());
        }
    }

    @Transactional
    public void setExchangeRateAlarm(ExchangeRateAlarmRequestDto requestDto){
        Member member = memberRepository.findById(requestDto.getMemberIdx()).orElseThrow(EntityNotFoundException::new);
        ExchangeRate exchangeRate = exchangeRateRepository.findExchangeRateByCurCode(requestDto.getCurCode());
        exchangeRateAlarmRepository.save(requestDto.toEntity(member,exchangeRate));
    }

    @Transactional
    public synchronized void checkNotifyAlarms() throws IOException {

        List<ExchangeRateAlarm> alarms = exchangeRateAlarmRepository.findAll();

        for(ExchangeRateAlarm alarm : alarms){
            if(alarm.getNotified()) {
                continue;
            }
            ExchangeRate exchangeRate = exchangeRateRepository.findExchangeRateByCurCode(alarm.getExchangeRate().getCurCode());
            BigDecimal currRate = exchangeRate.getCurRate();
            if(alarm.getExchangeRate().getCurCode().equals(exchangeRate.getCurCode())){
                boolean notify = false;
                Member member = memberRepository.findById(alarm.getMember().getMemberIdx()).orElseThrow(EntityNotFoundException::new);

                if (alarm.getRateType() == ExchangeRateAlarmType.OVER && currRate.compareTo(alarm.getCurRate()) >= 0) {
                    notify = true;
                    firebaseFCMService.sendMessageTo(FcmSendDto.builder().token(member.getFcmToken()).title("환율 알림").body("환율이 "+alarm.getCurRate()+" 이상에 도달했어요!").build());
                } else if (alarm.getRateType() == ExchangeRateAlarmType.LESS && currRate.compareTo(alarm.getCurRate()) <= 0) {
                    notify = true;
                    firebaseFCMService.sendMessageTo(FcmSendDto.builder().token(member.getFcmToken()).title("환율 알림").body("환율이 "+alarm.getCurRate()+" 이하에 도달했어요!").build());

                }
                if (notify) {
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


    @Transactional
    public void deleteAlarm(Long memberIdx, Long alarmIdx)  {
        ExchangeRateAlarm alarm = exchangeRateAlarmRepository.findByMember_MemberIdxAndAlarmIdx(memberIdx, alarmIdx)
                .orElseThrow(AlarmNotFoundException::new);
        exchangeRateAlarmRepository.delete(alarm);

    }

}
