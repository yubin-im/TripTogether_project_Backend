package com.hanaro.triptogether.country.service;

import com.hanaro.triptogether.country.dto.Country;

import java.util.List;

public interface CountryService {
    public List<Country> getAll();

    public List<Country> getCountriesByContinentId(Long id);
}
