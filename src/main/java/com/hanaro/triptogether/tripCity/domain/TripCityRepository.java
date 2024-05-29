package com.hanaro.triptogether.tripCity.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripCityRepository extends JpaRepository<TripCity, Long> {

    List<TripCity> findAllByTrip_TripIdx(Long tripIdx);
}
