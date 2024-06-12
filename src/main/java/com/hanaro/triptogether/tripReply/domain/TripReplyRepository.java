package com.hanaro.triptogether.tripReply.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripReplyRepository extends JpaRepository<TripReply, Long> {
    List<TripReply> findAllByTripPlace_TripPlaceIdxOrderByCreatedAtAsc(Long trip_place_idx);
    Long countByTripPlace_TripPlaceIdx(Long trip_place_idx);
}
