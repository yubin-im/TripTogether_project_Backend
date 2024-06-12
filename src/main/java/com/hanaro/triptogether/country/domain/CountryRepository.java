package com.hanaro.triptogether.country.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
    public List<CountryEntity> findAllByContinent_ContinentIdx(Long id);
}
