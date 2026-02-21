package ru.crud_app.service;


import org.springframework.data.domain.Page;
import ru.crud_app.dto.ProductCreateDTO;
import ru.crud_app.dto.ProductResponseDTO;
import ru.crud_app.dto.ProductUpdateDTO;
import ru.crud_app.entity.ProductCategory;

public interface ProductService {
    ProductResponseDTO createProduct(ProductCreateDTO productCreateDTO);
    ProductResponseDTO getProductById(Long id);
    Page<ProductResponseDTO> getAllProducts(int page, int size, ProductCategory category);
    ProductResponseDTO updateProduct(Long id, ProductUpdateDTO productUpdateDTO);
    void deleteProduct(Long id);
}
