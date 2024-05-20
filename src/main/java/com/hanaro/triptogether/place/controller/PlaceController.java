package com.hanaro.triptogether.place.controller;


import com.hanaro.triptogether.place.dto.Place;
import com.hanaro.triptogether.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
@Repository
public class PlaceController {

    private final PlaceService service;

    @GetMapping
    public ResponseEntity<List<Place>> getPlaces() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAll());
    }


    @GetMapping("/category/{category_idx}")
    public ResponseEntity<List<Place>> getPlacesByCategoryId(@PathVariable Long category_idx) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getPlacesByCategoryId(category_idx));
    }
}
