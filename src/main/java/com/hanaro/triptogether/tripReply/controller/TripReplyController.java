package com.hanaro.triptogether.tripReply.controller;

import com.hanaro.triptogether.tripReply.dto.response.TripReplyResDto;
import com.hanaro.triptogether.tripReply.service.TripReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trips/place")
public class TripReplyController {
    private final TripReplyService tripReplyService;

    @GetMapping("/{trip_place_idx}/reply")
    public List<TripReplyResDto> getReply(@PathVariable("trip_place_idx") Long trip_place_idx) {
        return tripReplyService.getReply(trip_place_idx);
    }
}
