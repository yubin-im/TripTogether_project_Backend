package com.hanaro.triptogether.continent.controller;

import com.hanaro.triptogether.continent.domain.ContinentEntity;
import com.hanaro.triptogether.continent.dto.Continent;
import com.hanaro.triptogether.continent.service.ContinentService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Timer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/continents")
@RequiredArgsConstructor
public class ContinentController {

    private final ContinentService service;

    @GetMapping
    public ResponseEntity<List<Continent>> getContinents() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAll());
    }
}
