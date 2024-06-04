package com.hanaro.triptogether.place.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceEntity, Long> {
    public List<PlaceEntity> findByCategoryIdx(Long category_id);

    @Query("SELECT p FROM PlaceEntity p WHERE (:category_id IS NULL OR p.categoryIdx = :category_id) AND (:city_id IS NULL OR p.city.cityIdx = :city_id)")
    List<PlaceEntity> findByCategoryIdxAndCityIdx(@Param("category_id") Long category_id, @Param("city_id") Long city_id);
}
