package ru.crud_app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.crud_app.dto.ProductCreateDTO;
import ru.crud_app.dto.ProductResponseDTO;
import ru.crud_app.dto.ProductUpdateDTO;
import ru.crud_app.entity.ProductCategory;
import ru.crud_app.service.ProductService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Создать новый продукт", description = "Возвращает созданный продукт со скидкой")
    @ApiResponse(responseCode = "201", description = "Продукт успешно создан")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации данных")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        ProductResponseDTO created = productService.createProduct(productCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить продукт по ID")
    @ApiResponse(responseCode = "200", description = "Продукт найден")
    @ApiResponse(responseCode = "404", description = "Продукт не найден")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @Parameter(description = "Уникальный идентификатор продукта") @PathVariable Long id) {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    @Operation(summary = "Получить список продуктов с пагинацией")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @Parameter(description = "Номер страницы")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Фильтр по категории")
            @RequestParam(required = false) ProductCategory category) {
        Page<ProductResponseDTO> products = productService.getAllProducts(page, size, category);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить продукт по ID")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @Parameter(description = "Уникальный идентификатор продукта") @PathVariable Long id,
            @Parameter(description = "Данные для обновления продукта") @Valid @RequestBody ProductUpdateDTO productUpdateDTO) {
        ProductResponseDTO updated = productService.updateProduct(id, productUpdateDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить продукт по ID")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Уникальный идентификатор продукта") @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
