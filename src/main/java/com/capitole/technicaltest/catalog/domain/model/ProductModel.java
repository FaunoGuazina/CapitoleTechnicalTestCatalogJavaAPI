package com.capitole.technicaltest.catalog.domain.model;

import java.math.BigDecimal;
import lombok.Builder;

@Builder(toBuilder = true)
public record ProductModel(
    String sku,
    BigDecimal price,
    String description,
    String category,
    BigDecimal discountedPrice,
    int discountApplied) {}
