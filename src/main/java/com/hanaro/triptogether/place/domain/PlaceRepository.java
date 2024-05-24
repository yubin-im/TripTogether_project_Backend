package com.hanaro.triptogether.place.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceEntity, Long> {
    public List<PlaceEntity> findByCategoryIdx(Long category_id);
}
