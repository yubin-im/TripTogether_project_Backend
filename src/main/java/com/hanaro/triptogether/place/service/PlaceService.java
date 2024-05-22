package com.hanaro.triptogether.place.service;

import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.place.domain.Place;
import com.hanaro.triptogether.place.domain.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    public Place findByPlaceIdx(Long placeIdx) {
        return placeRepository.findById(placeIdx).orElseThrow(()->new ApiException(ExceptionEnum.PLACE_NOT_FOUND));
    }
}
