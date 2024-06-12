package com.hanaro.triptogether.category.domain;

import com.hanaro.triptogether.category.dto.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
