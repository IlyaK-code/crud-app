package ru.crud_app.dto;

import jakarta.validation.constraints.*;
import ru.crud_app.entity.ProductCategory;

import java.math.BigDecimal;

public record ProductCreateDTO(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100)
        String name,

        @Size(max = 500)
        String description,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0.01")
        BigDecimal price,

        @NotNull(message = "Category is required")
        ProductCategory category
) {
}
