package ru.crud_app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.crud_app.dto.ProductCreateDTO;
import ru.crud_app.dto.ProductResponseDTO;
import ru.crud_app.dto.ProductUpdateDTO;
import ru.crud_app.entity.Product;
import ru.crud_app.entity.ProductCategory;
import ru.crud_app.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Реализация классического Service слоя
 * CRUD приложения для работы с товарами
 * Реализация интерфейса ProductService.
 */
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductCreateDTO productCreateDTO) {
        Product product = new Product(
                null,
                productCreateDTO.name(),
                productCreateDTO.description(),
                productCreateDTO.price(),
                productCreateDTO.category(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product saved = productRepository.save(product);
        return mapToResponseDTO(saved);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapToResponseDTO(product);
    }

    @Override
    public Page<ProductResponseDTO> getAllProducts(int page, int size, ProductCategory category) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);

        Page<Product> productPage = category != null
                ? productRepository.findByCategory(category, pageable)
                : productRepository.findAll(pageable);

        return productPage.map(this::mapToResponseDTO);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO productUpdateDTO) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (productUpdateDTO.name() != null) {
            product.setName(productUpdateDTO.name());
        }
        if (productUpdateDTO.description() != null) {
            product.setDescription(productUpdateDTO.description());
        }
        if (productUpdateDTO.price() != null) {
            product.setPrice(productUpdateDTO.price());
        }
        if (productUpdateDTO.category() != null) {
            product.setCategory(productUpdateDTO.category());
        }

        Product updated = productRepository.save(product);
        return mapToResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
    }

    /**
     * Маппинг Entity -> DTO.
     * Здесь мы вызываем расчет скидки и подставляем значение в ответ.
     * Решил не использовать MapStruct, т.к. не так много полей, проще самому реализовать маппер
     */
    private ProductResponseDTO mapToResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                calculateDiscountedPrice(product),
                product.getCategory(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    /**
     * Расчет цены со скидкой.
     */
    private BigDecimal calculateDiscountedPrice(Product product) {
        if (product.getCategory() == ProductCategory.BOOKS) {
            return product.getPrice().multiply(new BigDecimal("0.9"));
        }
        return product.getPrice();
    }
}
