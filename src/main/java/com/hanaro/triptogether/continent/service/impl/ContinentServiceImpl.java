package com.hanaro.triptogether.continent.service.impl;

import com.hanaro.triptogether.continent.domain.ContinentEntity;
import com.hanaro.triptogether.continent.domain.ContinentRepository;
import com.hanaro.triptogether.continent.dto.Continent;
import com.hanaro.triptogether.continent.service.ContinentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContinentServiceImpl implements ContinentService {

    private final ContinentRepository repository;

    @Override
    public List<Continent> getAll() {
        List<ContinentEntity> entities = repository.findAll();
        List<Continent> continents = entities.stream().map((entity) ->
            entity.toContinent()
        ).collect(Collectors.toList());

        return continents;
    }
}
