package com.hanaro.triptogether.trip.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findAllByTeam_TeamIdx(Long teamIdx);
    @Modifying
    @Query("UPDATE Trip t SET t.tripExpectedAmount = :tripExpectedAmount WHERE t.tripIdx = :tripIdx")
    void updateExpectedAmount(@Param("tripIdx") Long tripIdx, @Param("tripExpectedAmount") BigDecimal expectedAmount);
}
