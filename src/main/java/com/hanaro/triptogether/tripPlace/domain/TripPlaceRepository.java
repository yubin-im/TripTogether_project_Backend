package com.hanaro.triptogether.tripPlace.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface TripPlaceRepository extends JpaRepository<TripPlace, Long> {
    List<TripPlace> findAllByTrip_TripIdxOrderByTripDateAscPlaceOrderAsc(Long tripIdx);

    @Query("SELECT COUNT(tp) FROM TripPlace tp WHERE tp.trip.tripIdx = :tripId and tp.tripDate = :tripDate")
    Integer countByTripIdAndTripDate(Long tripId, Integer tripDate);

    @Query("SELECT COUNT(tp) FROM TripPlace tp WHERE tp.trip.tripIdx = :tripId")
    Integer countByTripId(Long tripId);
    @Modifying
    @Query("UPDATE TripPlace tp SET tp.placeOrder = tp.placeOrder - 1 WHERE tp.trip.tripIdx = :tripIdx AND tp.placeOrder > :placeOrder")
    void decrementPlaceOrderAfter(@Param("tripIdx") Long tripIdx, @Param("placeOrder") Integer placeOrder);

    @Query("SELECT sum(tp.placeAmount) FROM TripPlace tp WHERE tp.trip.tripIdx= :tripIdx")
    BigDecimal getSumPlaceAmountByTripIdx(Long tripIdx);
}
