package com.hanaro.triptogether.trip.controller;

import com.hanaro.triptogether.trip.dto.request.TripReqDto;
import com.hanaro.triptogether.trip.dto.response.TripResDto;
import com.hanaro.triptogether.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trips")
public class TripController {
    private final TripService tripService;

    @GetMapping("/{trip_idx}")
    public TripResDto getTrip(@PathVariable("trip_idx") Long trip_idx) {
        return tripService.getTrip(trip_idx);
    }

    @GetMapping("/teams/{team_idx}")
    public List<TripResDto> getTrips(@PathVariable("team_idx") Long team_idx) {
        return tripService.getTripsByTeam(team_idx);
    }

    @PostMapping
    public ResponseEntity<?> createTrip(@RequestBody TripReqDto reqDto) {
        try {
            tripService.createTrip(reqDto);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{trip_idx}")
    public ResponseEntity<?> updateTrip(@PathVariable("trip_idx") Long trip_idx, @RequestBody TripReqDto reqDto) {
        try {
            tripService.updateTrip(trip_idx, reqDto);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{trip_idx}")
    public ResponseEntity<?> deleteTrip(@PathVariable("trip_idx") Long trip_idx) {
        try {
            tripService.deleteTrip(trip_idx);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
