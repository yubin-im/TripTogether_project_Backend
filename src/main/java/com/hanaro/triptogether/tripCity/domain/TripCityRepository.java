package com.hanaro.triptogether.tripCity.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripCityRepository extends JpaRepository<TripCity, Long> {
}
