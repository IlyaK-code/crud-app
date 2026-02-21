package ru.crud_app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.crud_app.entity.Product;
import ru.crud_app.entity.ProductCategory;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findById(@Param("id") Long id);

    @Query("SELECT p FROM Product p ORDER BY p.createdAt DESC ")
    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category = :category ORDER BY p.createdAt DESC ")
    Page<Product> findByCategory(@Param("category") ProductCategory category, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.id = :id")
    boolean existsById(@Param("id") Long id);
}
