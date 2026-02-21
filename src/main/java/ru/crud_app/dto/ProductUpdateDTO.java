package ru.crud_app.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.crud_app.entity.ProductCategory;

import java.math.BigDecimal;

public record ProductUpdateDTO(
        @Size(min = 2, max = 100)
        String name,

        @Size(max = 500)
        String description,

        @Positive(message = "Price must be positive")
        BigDecimal price,

        ProductCategory category
) {
}
