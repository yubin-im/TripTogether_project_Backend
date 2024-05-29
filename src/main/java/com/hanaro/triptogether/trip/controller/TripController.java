package com.hanaro.triptogether.trip.controller;

import com.hanaro.triptogether.trip.dto.response.TripResDto;
import com.hanaro.triptogether.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trips")
public class TripController {
    private final TripService tripService;

    @GetMapping("/teams/{team_idx}")
    public List<TripResDto> getTrips(@PathVariable("team_idx") Long team_idx) {
        return tripService.getTripsByTeam(team_idx);
    }
}
