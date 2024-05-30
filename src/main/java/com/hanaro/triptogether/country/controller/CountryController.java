package com.hanaro.triptogether.country.controller;

import com.hanaro.triptogether.country.dto.Country;
import com.hanaro.triptogether.country.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService service;

    @GetMapping
    public ResponseEntity<List<Country>> getCountries() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAll());
    }

    @GetMapping("/continents/{continent_idx}")
    public ResponseEntity<List<Country>> getCountriesByContinentId(@PathVariable Long continent_idx) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getCountriesByContinentId(continent_idx));
    }
}
