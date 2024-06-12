package com.hanaro.triptogether.city.controller;

import com.hanaro.triptogether.city.dto.City;
import com.hanaro.triptogether.city.service.CityService;
import com.hanaro.triptogether.city.service.impl.CityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService service;

    @GetMapping
    public ResponseEntity<List<City>> getCities() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAll());
    }

    @GetMapping("/countries/{country_idx}")
    public ResponseEntity<List<City>> getCitiesByCountryId(@PathVariable Long country_idx) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getCitiesByCountryId(country_idx));
    }
}
