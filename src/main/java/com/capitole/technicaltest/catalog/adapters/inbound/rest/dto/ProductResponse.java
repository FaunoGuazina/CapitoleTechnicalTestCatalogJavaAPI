package com.capitole.technicaltest.catalog.adapters.inbound.rest.dto;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record ProductResponse(
    String sku,
    String description,
    String category,
    BigDecimal price,
    BigDecimal discountedPrice,
    int discountApplied) {}
