package com.hanaro.triptogether.tripReply.controller;

import com.hanaro.triptogether.tripReply.dto.request.TripReplyDeleteReqDto;
import com.hanaro.triptogether.tripReply.dto.request.TripReplyReqDto;
import com.hanaro.triptogether.tripReply.dto.request.TripReplyUpdateReqDto;
import com.hanaro.triptogether.tripReply.dto.response.TripReplyResDto;
import com.hanaro.triptogether.tripReply.service.TripReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trips/place")
public class TripReplyController {
    private final TripReplyService tripReplyService;

    @PostMapping("/{trip_place_idx}/reply")
    public void createReply(@PathVariable("trip_place_idx") Long trip_place_idx, @RequestBody TripReplyReqDto dto) {
        tripReplyService.createReply(trip_place_idx, dto);
    }

    @PutMapping("/{trip_place_idx}/reply")
    public void updateReply(@PathVariable("trip_place_idx") Long trip_place_idx, @RequestBody TripReplyUpdateReqDto dto) {
        tripReplyService.updateReply(trip_place_idx, dto);
    }

    @GetMapping("/{trip_place_idx}/reply")
    public List<TripReplyResDto> getReply(@PathVariable("trip_place_idx") Long trip_place_idx) {
        return tripReplyService.getReply(trip_place_idx);
    }

    @DeleteMapping("/{trip_place_idx}/reply")
    public void deleteReply(@PathVariable("trip_place_idx") Long trip_place_idx, @RequestBody TripReplyDeleteReqDto dto) {
        tripReplyService.deleteReply(trip_place_idx, dto);
    }
}
