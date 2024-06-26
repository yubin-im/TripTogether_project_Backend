package com.hanaro.triptogether.tripPlace.controller;

import com.hanaro.triptogether.tripPlace.dto.request.TripPlaceAddReqDto;
import com.hanaro.triptogether.tripPlace.dto.request.TripPlaceUpdateInfoReqDto;
import com.hanaro.triptogether.tripPlace.dto.request.TripPlaceUpdateReqDto;
import com.hanaro.triptogether.tripPlace.dto.request.UpdateOrderReqDto;
import com.hanaro.triptogether.tripPlace.dto.response.TripPlaceResDto;
import com.hanaro.triptogether.tripPlace.service.TripPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trips/place")
public class TripPlaceController {

    private final TripPlaceService tripPlaceService;

    @PostMapping("")
    public void addPlace(@RequestBody TripPlaceAddReqDto dto) {
        tripPlaceService.addPlace(dto);
    }

    @PutMapping("/info/{trip_place_idx}")
    public void updatePlace(@PathVariable("trip_place_idx") Long trip_place_idx, @RequestBody TripPlaceUpdateInfoReqDto dto) {
        tripPlaceService.updatePlace(trip_place_idx, dto);
    }
    @PutMapping("/order/{trip_idx}")
    public void updatePlaceOrder(@PathVariable("trip_idx") Long trip_idx, @RequestBody UpdateOrderReqDto dto) {
        tripPlaceService.updatePlaceOrder(trip_idx, dto);
    }

    @GetMapping("/{trip_idx}")
    public List<TripPlaceResDto> getPlace(@PathVariable("trip_idx") Long trip_idx) {
        return tripPlaceService.getPlace(trip_idx);
    }

    @DeleteMapping("/{trip_place_idx}")
    public void deletePlace(@PathVariable("trip_place_idx") Long trip_place_idx) {
        tripPlaceService.deleteTripPlace(trip_place_idx);
    }


    @PutMapping("/{trip_idx}")
    public void updateTripPlace(@PathVariable("trip_idx") Long trip_idx, @RequestBody TripPlaceUpdateReqDto dto) {
        tripPlaceService.updateTripPlace(trip_idx, dto);
    }

}
