package ru.crud_app.dto;

import ru.crud_app.entity.Product;
import ru.crud_app.entity.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        BigDecimal discountedPrice,
        ProductCategory category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
