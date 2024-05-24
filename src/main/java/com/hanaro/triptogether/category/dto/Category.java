package com.hanaro.triptogether.category.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Category {
    Long categoryIdx;
    String categoryName;
}
