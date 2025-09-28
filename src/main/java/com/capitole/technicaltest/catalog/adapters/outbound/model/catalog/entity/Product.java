package com.capitole.technicaltest.catalog.adapters.outbound.model.catalog.entity;

import java.math.BigDecimal;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "products")
public record Product(
    @Id String id, String sku, BigDecimal price, String description, String category) {}
