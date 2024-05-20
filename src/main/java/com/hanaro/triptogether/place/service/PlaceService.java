package com.hanaro.triptogether.place.service;

import com.hanaro.triptogether.place.dto.Place;

import java.util.List;

public interface PlaceService {
    public List<Place> getAll();
    public List<Place> getPlacesByCategoryId(Long categoryId);
}
