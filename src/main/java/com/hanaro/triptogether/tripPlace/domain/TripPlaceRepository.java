package com.hanaro.triptogether.tripPlace.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TripPlaceRepository extends JpaRepository<TripPlace, Long> {
    List<TripPlace> findAllByTrip_TripIdxOrderByTripDateAscPlaceOrderAsc(Long tripIdx);

    @Query("SELECT COUNT(tp) FROM TripPlace tp WHERE tp.trip.tripIdx = :tripId and tp.tripDate = :tripDate")
    Integer countByTripId(Long tripId, Integer tripDate);

}
