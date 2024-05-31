package com.hanaro.triptogether.account.domain;

import com.hanaro.triptogether.accountTransactionDetails.AccountTransactionDetails;
import com.hanaro.triptogether.dues.dto.response.DuesListMemberResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AccountTransactionDetailsRepository extends JpaRepository<AccountTransactionDetails,Long> {


    @Query(value = "select NEW  com.hanaro.triptogether.dues.dto.response.DuesListMemberResponseDto(atd.account.member.memberName,sum(atd.transAmount)) " +
            "from AccountTransactionDetails atd where atd.account.accIdx = :accIdx" +
            " AND FUNCTION('YEAR', atd.transDate) = :year " +
            "AND FUNCTION('MONTH', atd.transDate) = :month " +
            "GROUP BY atd.account.accIdx " +
            "HAVING SUM(atd.transAmount) >= :duesAmount")
    List<DuesListMemberResponseDto> findUsersWithTransAmountGreaterThanOrEqual(
            @Param("accIdx") Long accIdx,
            @Param("year") int year,
            @Param("month") int month,
            @Param("duesAmount") BigDecimal duesAmount);

    @Query("SELECT NEW com.hanaro.triptogether.dues.dto.response.DuesListMemberResponseDto(atd.account.member.memberName, SUM(atd.transAmount)) " +
            "FROM AccountTransactionDetails atd " +
            "WHERE atd.account.accIdx = :accIdx " +
            "AND FUNCTION('YEAR', atd.transDate) = :year " +
            "AND FUNCTION('MONTH', atd.transDate) = :month " +
            "GROUP BY atd.account.accIdx " +
            "HAVING SUM(atd.transAmount) < :duesAmount")
    List<DuesListMemberResponseDto> findUsersWithTransAmountLessThan(
            @Param("accIdx") Long accIdx,
            @Param("year") int year,
            @Param("month") int month,
            @Param("duesAmount") BigDecimal duesAmount);

    @Query("SELECT SUM(atd.transAmount) FROM AccountTransactionDetails atd " +
            "WHERE atd.account.accIdx = :accIdx " +
            "AND FUNCTION('YEAR', atd.transDate) = :year " +
            "AND FUNCTION('MONTH', atd.transDate) = :month")
    BigDecimal findTotalTransAmountByAccIdxAndYearAndMonth(
            @Param("accIdx") Long accIdx,
            @Param("year") int year,
            @Param("month") int month);
}

