package com.hanaro.triptogether.city.domain;

import com.hanaro.triptogether.city.dto.City;
import com.hanaro.triptogether.country.dto.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {
    public List<CityEntity> findAllByCountry_CountryIdx(Long id);
}
