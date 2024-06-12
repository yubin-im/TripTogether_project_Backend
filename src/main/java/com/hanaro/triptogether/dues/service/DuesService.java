package com.hanaro.triptogether.dues.service;

import com.hanaro.triptogether.account.domain.AccountTransactionDetailsRepository;
import com.hanaro.triptogether.dues.domain.entity.Dues;
import com.hanaro.triptogether.dues.domain.repository.DuesRepository;
import com.hanaro.triptogether.dues.dto.request.DuesRuleRequestDto;
import com.hanaro.triptogether.dues.dto.response.*;
import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.teamMember.domain.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DuesService {

    private final DuesRepository duesRepository;
    private final AccountTransactionDetailsRepository accountTransactionDetailsRepository;
    private final TeamMemberRepository teamMemberRepository;


    public DuesDetailTotalAmountResponseDto getDuesDetailTotalAmount(Long accIdx, Long memberIdx) {
        return accountTransactionDetailsRepository.findSumOfTransAmountByMemberIdx(accIdx,memberIdx);
    }

    public void deleteDuesRule(Long teamIdx){
        Dues dues = duesRepository.findDuesByTeamIdx(teamIdx);
        dues.deleteDuesRule();
        duesRepository.save(dues);
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
        }else if(dues.getIsDeleted()){
            return null;
        }
        return DuesRuleResponseDto.builder().duesDate(String.valueOf(dues.getDuesDate())).duesAmount(dues.getDuesAmount()).build();
    }


    public DuesListResponseDto getDuesList(Long teamIdx,Long accIdx, YearMonth date,Boolean paid){
        List<DuesListMemberResponseDto> duesListMemberResponseDtos = new ArrayList<>();
        BigDecimal duesTotalAmount;

        List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamIdx(teamIdx);

        BigDecimal teamDuesAmount = getTeamDuesAmount(teamIdx);

        if (paid) {
            for (TeamMember member : teamMembers) {
                DuesListMemberResponseDto responseDto = accountTransactionDetailsRepository.findUsersWithTransAmountGreaterThanOrEqual(accIdx, member.getMember().getMemberIdx(), date.getYear(), date.getMonthValue(), teamDuesAmount);
                if (responseDto != null) {
                    duesListMemberResponseDtos.add(responseDto);
                }
            }
        } else {
            for (TeamMember member : teamMembers) {
                DuesListMemberResponseDto responseDto = accountTransactionDetailsRepository.findUsersWithTransAmountLessThan(accIdx, member.getMember().getMemberIdx(), date.getYear(), date.getMonthValue(), teamDuesAmount);
                DuesListMemberResponseDto paidResponseDto = accountTransactionDetailsRepository.findUsersWithTransAmountGreaterThanOrEqual(accIdx, member.getMember().getMemberIdx(), date.getYear(), date.getMonthValue(), teamDuesAmount);

                if (responseDto != null) {
                    duesListMemberResponseDtos.add(responseDto);
                } else if (paidResponseDto != null ) {
                    continue;
                } else {
                    duesListMemberResponseDtos.add(DuesListMemberResponseDto.builder()
                            .memberName(member.getMember().getMemberName())
                            .memberIdx(member.getMember().getMemberIdx())
                            .memberAmount(BigDecimal.ZERO)
                            .build());
                }
            }
        }
        duesTotalAmount = accountTransactionDetailsRepository.findTotalTransAmountByAccIdxAndYearAndMonth(accIdx,date.getYear(),date.getMonthValue());
        if (duesTotalAmount == null) {
            duesTotalAmount = BigDecimal.ZERO;
        }
        return DuesListResponseDto.builder().duesTotalAmount(duesTotalAmount).memberResponseDtos(duesListMemberResponseDtos).build();
    }

    private BigDecimal getTeamDuesAmount(Long teamIdx){
        Dues dues = duesRepository.findDuesByTeamIdx(teamIdx);
        if (dues == null) {
            return null;
        }
        return dues.getDuesAmount();
    }

}
