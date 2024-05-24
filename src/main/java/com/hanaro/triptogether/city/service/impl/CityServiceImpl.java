package com.hanaro.triptogether.city.service.impl;

import com.hanaro.triptogether.city.domain.CityEntity;
import com.hanaro.triptogether.city.domain.CityRepository;
import com.hanaro.triptogether.city.dto.City;
import com.hanaro.triptogether.city.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository repository;

    @Override
    public List<City> getAll() {
        List<CityEntity> entities = repository.findAll();
        List<City> cities = entities.stream().map((entity) -> entity.toCity())
                .collect(Collectors.toList());

        return cities;
    }

    @Override
    public List<City> getCitiesByCountryId(Long countryId) {
        List<CityEntity> entities = repository.findAllByCountry_CountryIdx(countryId);
        List<City> cities = entities.stream().map((entity) -> entity.toCity())
                .collect(Collectors.toList());

        return cities;
    }
}
