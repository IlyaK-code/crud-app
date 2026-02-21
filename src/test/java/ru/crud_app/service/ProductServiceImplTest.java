package ru.crud_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.crud_app.dto.ProductCreateDTO;
import ru.crud_app.dto.ProductResponseDTO;
import ru.crud_app.dto.ProductUpdateDTO;
import ru.crud_app.entity.Product;
import ru.crud_app.entity.ProductCategory;
import ru.crud_app.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;
    private Product testProduct;
    private ProductCreateDTO createDTO;
    private ProductUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        // Подготовка тестовых данных
        testProduct = new Product(
                1L, "Java Book", "Best Java book",
                new BigDecimal("1000.00"), ProductCategory.BOOKS,
                LocalDateTime.now(), LocalDateTime.now()
        );

        createDTO = new ProductCreateDTO(
                "Java Book", "Best Java book",
                new BigDecimal("1000.00"), ProductCategory.BOOKS
        );

        updateDTO = new ProductUpdateDTO(
                null, null, new BigDecimal("1500.00"), null
        );
    }

    @Test
    @DisplayName("Скидка 10% применяется к категории BOOKS")
    void calculateDiscountedPrice_booksCategory_appliesDiscount() {
        Product book = new Product(
                null, "Test Book", null,
                new BigDecimal("100.00"), ProductCategory.BOOKS,
                null, null
        );

        // используем вспомогательный метод для расчета скидки
        BigDecimal discountedPrice = invokeCalculateDiscountedPrice(book);

        assertThat(discountedPrice).isEqualByComparingTo(new BigDecimal("90.00"));
    }

    @Test
    @DisplayName("Скидка не применяется к другим категориям")
    void calculateDiscountedPrice_otherCategory_noDiscount() {
        Product electronics = new Product(
                null, "Laptop", null,
                new BigDecimal("50000.00"), ProductCategory.ELECTRONICS,
                null, null
        );

        BigDecimal price = invokeCalculateDiscountedPrice(electronics);

        assertThat(price).isEqualTo(new BigDecimal("50000.00"));
    }


    // ТЕСТЫ ДЛЯ CRUD ↓

    @Test
    @DisplayName("createProduct: сохраняет продукт и возвращает DTO со скидкой")
    void createProduct_success_returnsDTOWithDiscount() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductResponseDTO result = productService.createProduct(createDTO);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Java Book");
        assertThat(result.discountedPrice()).isEqualByComparingTo(new BigDecimal("900.00"));

        // Проверяем, что save() был вызван ровно 1 раз
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("getProductById: возвращает продукт, если он найден")
    void getProductById_found_returnsDTO() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        ProductResponseDTO result = productService.getProductById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.discountedPrice()).isEqualByComparingTo(new BigDecimal("900.00"));
    }

    @Test
    @DisplayName("getProductById: выбрасывает исключение, если продукт не найден")
    void getProductById_notFound_throwsException() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found with id: 999");
    }

    @Test
    @DisplayName("updateProduct: обновляет только переданные поля")
    void updateProduct_partialUpdate_onlyPriceChanged() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponseDTO result = productService.updateProduct(1L, updateDTO);

        assertThat(result.price()).isEqualTo(new BigDecimal("1500.00")); // Цена обновлена
        assertThat(result.name()).isEqualTo("Java Book"); // Имя не изменилось

        // Проверяем, что в save() передан продукт с новой ценой
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        Product saved = captor.getValue();
        assertThat(saved.getPrice()).isEqualTo(new BigDecimal("1500.00"));
        assertThat(saved.getName()).isEqualTo("Java Book");
    }

    @Test
    @DisplayName("getAllProducts: возвращает страницу с продуктами")
    void getAllProducts_withCategory_returnsFilteredPage() {
        Page<Product> productPage = new PageImpl<>(List.of(testProduct));
        when(productRepository.findByCategory(ProductCategory.BOOKS, PageRequest.of(0, 10)))
                .thenReturn(productPage);

        Page<ProductResponseDTO> result = productService.getAllProducts(0, 10, ProductCategory.BOOKS);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).category()).isEqualTo(ProductCategory.BOOKS);
    }

    // ВСПОМОГАТЕЛЬНЫЙ МЕТОД ↓

    /**
     * Вспомогательный метод для вызова private метода calculateDiscountedPrice через reflection.
     */
    private BigDecimal invokeCalculateDiscountedPrice(Product product) {
        try {
            var method = ProductServiceImpl.class
                    .getDeclaredMethod("calculateDiscountedPrice", Product.class);
            method.setAccessible(true);
            return (BigDecimal) method.invoke(productService, product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
