package com.hanaro.triptogether.place.service.impl;

import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.place.domain.PlaceEntity;
import com.hanaro.triptogether.place.domain.PlaceRepository;
import com.hanaro.triptogether.place.dto.Place;
import com.hanaro.triptogether.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository repository;

    @Override
    public List<Place> getAll() {
       List<PlaceEntity> entities = repository.findAll();
       List<Place> places = entities.stream().map((entity) -> entity.toPlace())
               .collect(Collectors.toList());

       return places;
    }

    @Override
    public List<Place> getPlacesByCategoryId(Long category_id) {
        List<PlaceEntity> entities = repository.findByCategoryIdx(category_id);
        List<Place> places = entities.stream().map((entity) -> entity.toPlace())
                .collect(Collectors.toList());

        return places;
    }

    @Override
    public List<Place> getPlacesByCategoryIdAndCityId(Long category_id, Long city_id) {
        List<PlaceEntity> entities = repository.findByCategoryIdxAndCityIdx(category_id, city_id);
        List<Place> places = entities.stream().map((entity) -> entity.toPlace())
                .collect(Collectors.toList());

        return places;
    };

    @Override
    public PlaceEntity findByPlaceIdx(Long placeIdx) {
        return repository.findById(placeIdx).orElseThrow(()->new ApiException(ExceptionEnum.PLACE_NOT_FOUND));
    }
}
