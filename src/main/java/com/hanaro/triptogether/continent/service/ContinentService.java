package com.hanaro.triptogether.continent.service;

import com.hanaro.triptogether.continent.domain.ContinentEntity;
import com.hanaro.triptogether.continent.dto.Continent;

import java.util.List;

public interface ContinentService {
    public List<Continent> getAll();
}
