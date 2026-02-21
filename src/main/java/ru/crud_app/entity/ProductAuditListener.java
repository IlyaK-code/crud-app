package ru.crud_app.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class ProductAuditListener {

    @PrePersist
    public void prePersist(Product product) {
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);
    }

    @PreUpdate
    public void preUpdate(Product product) {
        product.setUpdatedAt(LocalDateTime.now());
    }
}
