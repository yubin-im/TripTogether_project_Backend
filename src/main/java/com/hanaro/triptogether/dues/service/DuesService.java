package com.hanaro.triptogether.dues.service;

import com.hanaro.triptogether.account.domain.AccountTransactionDetailsRepository;
import com.hanaro.triptogether.dues.domain.entity.Dues;
import com.hanaro.triptogether.dues.domain.repository.DuesRepository;
import com.hanaro.triptogether.dues.dto.request.DuesRuleRequestDto;
import com.hanaro.triptogether.dues.dto.response.DuesListMemberResponseDto;
import com.hanaro.triptogether.dues.dto.response.DuesListResponseDto;
import com.hanaro.triptogether.dues.dto.response.DuesRuleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DuesService {

    private final DuesRepository duesRepository;
    private final AccountTransactionDetailsRepository accountTransactionDetailsRepository;

    public void setDuesRule(DuesRuleRequestDto duesRuleRequestDto){
        duesRepository.save(duesRuleRequestDto.toEntity());
    }

    public DuesRuleResponseDto getDuesRule(Long teamIdx){
        Dues dues = duesRepository.findDuesByTeamIdx(teamIdx);
        if (dues == null) {
            return null;
        }
        return DuesRuleResponseDto.builder().duesDate(String.valueOf(dues.getDuesDate().getDayOfMonth())).duesAmount(dues.getDuesAmount()).build();
    }


    public DuesListResponseDto getDuesList(Long teamIdx,Long accIdx, YearMonth date,Boolean paid){
        List<DuesListMemberResponseDto> duesListMemberResponseDtos;
        BigDecimal duesTotalAmount;
        if(paid) {
            duesListMemberResponseDtos = accountTransactionDetailsRepository.findUsersWithTransAmountGreaterThanOrEqual(accIdx, date.getYear(), date.getMonthValue(), getTeamDuesAmount(teamIdx));
        }
        else {
            duesListMemberResponseDtos = accountTransactionDetailsRepository.findUsersWithTransAmountLessThan(accIdx, date.getYear(), date.getMonthValue(), getTeamDuesAmount(teamIdx));
        }
        duesTotalAmount = accountTransactionDetailsRepository.findTotalTransAmountByAccIdxAndYearAndMonth(accIdx,date.getYear(),date.getMonthValue());
        return DuesListResponseDto.builder().duesTotalAmount(duesTotalAmount).memberResponseDtos(duesListMemberResponseDtos).build();
    }

    private BigDecimal getTeamDuesAmount(Long teamIdx){
        Dues dues = duesRepository.findDuesByTeamIdx(teamIdx);
        return dues.getDuesAmount();
    }

}
