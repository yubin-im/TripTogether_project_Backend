package com.hanaro.triptogether.dues.service;

import com.hanaro.triptogether.account.domain.AccountRepository;
import com.hanaro.triptogether.account.domain.AccountTransactionDetailsRepository;
import com.hanaro.triptogether.dues.domain.entity.Dues;
import com.hanaro.triptogether.dues.domain.repository.DuesRepository;
import com.hanaro.triptogether.dues.dto.request.DuesRuleRequestDto;
import com.hanaro.triptogether.dues.dto.response.DuesListResponseDto;
import com.hanaro.triptogether.team.domain.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DuesService {

    private final DuesRepository duesRepository;
    private final AccountRepository accountRepository;
    private final AccountTransactionDetailsRepository accountTransactionDetailsRepository;
    private final TeamRepository teamRepository;

    public void setDuesRule(DuesRuleRequestDto duesRuleRequestDto){
        duesRepository.save(duesRuleRequestDto.toEntity());
    }




    public List<DuesListResponseDto> getDuesList(Long teamIdx,Long accIdx, YearMonth date,Boolean paid){
        if(paid) return accountTransactionDetailsRepository.findUsersWithTransAmountGreaterThanOrEqual(accIdx,date.getYear(),date.getMonthValue(),getTeamDuesAmount(teamIdx));
        else return accountTransactionDetailsRepository.findUsersWithTransAmountLessThan(accIdx,date.getYear(),date.getMonthValue(),getTeamDuesAmount(teamIdx));
    }

    private BigDecimal getTeamDuesAmount(Long teamIdx){
        Dues dues = duesRepository.findDuesByTeamIdx(teamIdx);
        return dues.getDuesAmount();
    }

}
