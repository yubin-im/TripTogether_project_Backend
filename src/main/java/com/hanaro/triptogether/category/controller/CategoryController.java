package com.hanaro.triptogether.category.controller;

import com.hanaro.triptogether.category.dto.Category;
import com.hanaro.triptogether.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    ResponseEntity<List<Category>> getCategory() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAll());
    }
}
