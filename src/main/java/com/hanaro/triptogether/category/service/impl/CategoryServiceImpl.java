package com.hanaro.triptogether.category.service.impl;

import com.hanaro.triptogether.category.domain.CategoryEntity;
import com.hanaro.triptogether.category.domain.CategoryRepository;
import com.hanaro.triptogether.category.dto.Category;
import com.hanaro.triptogether.category.service.CategoryService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public List<Category> getAll() {
        List<CategoryEntity> entities = repository.findAll();
        List<Category> categories = entities.stream().map((entity) -> entity.toCategory())
                .collect(Collectors.toList());

        return categories;
    }
}
