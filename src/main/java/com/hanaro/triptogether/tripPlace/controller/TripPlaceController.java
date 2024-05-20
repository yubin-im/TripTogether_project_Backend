package com.hanaro.triptogether.tripPlace.controller;

import com.hanaro.triptogether.tripPlace.dto.response.TripPlaceResDto;
import com.hanaro.triptogether.tripPlace.service.TripPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trip/place")
public class TripPlaceController {

    private final TripPlaceService tripPlaceService;

    @GetMapping("/{trip_idx}")
    public List<TripPlaceResDto> getPlace(@PathVariable("trip_idx") Long trip_idx) {
        return tripPlaceService.getPlace(trip_idx);
    }

}
