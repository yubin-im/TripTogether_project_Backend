package com.hanaro.triptogether.country.service.impl;

import com.hanaro.triptogether.country.domain.CountryEntity;
import com.hanaro.triptogether.country.domain.CountryRepository;
import com.hanaro.triptogether.country.dto.Country;
import com.hanaro.triptogether.country.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository repository;

    @Override
    public List<Country> getAll() {
        List<CountryEntity> entities = repository.findAll();
        List<Country> countries = entities.stream().map((entity) -> entity.toCountry())
                .collect(Collectors.toList());

        return countries;
    }

    @Override
    public List<Country> getCountriesByContinentId(Long id) {
        List<CountryEntity> entities = repository.findAllByContinent_ContinentIdx(id);
        List<Country> countries = entities.stream().map((entity) -> entity.toCountry())
                .collect(Collectors.toList());

        return countries;
    }
}
