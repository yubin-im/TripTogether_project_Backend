package com.hanaro.triptogether.place.controller;


import com.hanaro.triptogether.place.dto.Place;
import com.hanaro.triptogether.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
@Repository
public class PlaceController {

    private final PlaceService service;

    @GetMapping
    public ResponseEntity<List<Place>> getPlacesByCategoryIdAndCityId(@RequestParam(required = false) Long category_id,
                                                                      @RequestParam(required = false) Long city_id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getPlacesByCategoryIdAndCityId(category_id, city_id));
    }
    @GetMapping("/categories/{category_idx}")
    public ResponseEntity<List<Place>> getPlacesByCategoryId(@PathVariable Long category_idx) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getPlacesByCategoryId(category_idx));
    }
}
