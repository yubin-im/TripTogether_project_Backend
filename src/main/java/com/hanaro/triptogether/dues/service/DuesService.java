package com.hanaro.triptogether.dues.service;

import com.hanaro.triptogether.account.domain.AccountTransactionDetailsRepository;
import com.hanaro.triptogether.dues.domain.entity.Dues;
import com.hanaro.triptogether.dues.domain.repository.DuesRepository;
import com.hanaro.triptogether.dues.dto.request.DuesDetailRequestDto;
import com.hanaro.triptogether.dues.dto.request.DuesRuleRequestDto;
import com.hanaro.triptogether.dues.dto.response.*;
import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.teamMember.domain.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DuesService {

    private final DuesRepository duesRepository;
    private final AccountTransactionDetailsRepository accountTransactionDetailsRepository;
    private final TeamMemberRepository teamMemberRepository;


    public DuesDetailTotalAmountResponseDto getDuesDetailTotalAmount(DuesDetailRequestDto duesDetailRequestDto) {
        return accountTransactionDetailsRepository.findSumOfTransAmountByMemberIdx(duesDetailRequestDto.getAccIdx(),duesDetailRequestDto.getMemberIdx());
    }

    public List<DuesDetailYearTotalAmountResponseDto> getDuesDetailByMonthAmount(Long accIdx, Long memberIdx, String duesYear){
        return accountTransactionDetailsRepository.findMonthlySumOfTransAmountByAccIdxAndMemberIdxAndYear(accIdx,memberIdx, Integer.parseInt(duesYear));
    }

    public void setDuesRule(DuesRuleRequestDto duesRuleRequestDto){
        Dues curDues = duesRepository.findDuesByTeamIdx(duesRuleRequestDto.getTeamIdx());
        if (curDues !=null) {
            curDues.modifyDuesRule(duesRuleRequestDto);
            duesRepository.save(curDues);
        } else {
            duesRepository.save(duesRuleRequestDto.toEntity());
        }
    }

    public DuesRuleResponseDto getDuesRule(Long teamIdx){
        Dues dues = duesRepository.findDuesByTeamIdx(teamIdx);
        if (dues == null) {
            return null;
        }
        return DuesRuleResponseDto.builder().duesDate(String.valueOf(dues.getDuesDate())).duesAmount(dues.getDuesAmount()).build();
    }


    public DuesListResponseDto getDuesList(Long teamIdx,Long accIdx, YearMonth date,Boolean paid){
        List<DuesListMemberResponseDto> duesListMemberResponseDtos = new ArrayList<>();
        BigDecimal duesTotalAmount;

        List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamIdx(teamIdx);

        if(paid) {
            for (TeamMember member :teamMembers) {

                DuesListMemberResponseDto responseDto = accountTransactionDetailsRepository.findUsersWithTransAmountGreaterThanOrEqual(accIdx, member.getMember().getMemberIdx(), date.getYear(), date.getMonthValue(), getTeamDuesAmount(teamIdx));
                if (responseDto != null){
                    duesListMemberResponseDtos.add(responseDto);
                }
            }
        }
        else {
            for (TeamMember member :teamMembers) {
                DuesListMemberResponseDto responseDto = accountTransactionDetailsRepository.findUsersWithTransAmountLessThan(accIdx, member.getMember().getMemberIdx(), date.getYear(), date.getMonthValue(), getTeamDuesAmount(teamIdx));
                if (responseDto != null){
                    duesListMemberResponseDtos.add(responseDto);
                }else {
                    duesListMemberResponseDtos.add(DuesListMemberResponseDto.builder().memberName(member.getMember().getMemberName()).memberIdx(member.getMember().getMemberIdx()).memberAmount(BigDecimal.valueOf(0)).build());

                }
            }
        }
        duesTotalAmount = accountTransactionDetailsRepository.findTotalTransAmountByAccIdxAndYearAndMonth(accIdx,date.getYear(),date.getMonthValue());
        return DuesListResponseDto.builder().duesTotalAmount(duesTotalAmount).memberResponseDtos(duesListMemberResponseDtos).build();
    }

    private BigDecimal getTeamDuesAmount(Long teamIdx){
        Dues dues = duesRepository.findDuesByTeamIdx(teamIdx);
        return dues.getDuesAmount();
    }

}
