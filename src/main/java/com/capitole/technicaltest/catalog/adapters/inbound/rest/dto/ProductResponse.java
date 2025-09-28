package com.capitole.technicaltest.catalog.adapters.inbound.rest.dto;

import java.math.BigDecimal;
import lombok.Builder;
import org.springframework.hateoas.server.core.Relation;

@Builder
@Relation(collectionRelation = "content")
public record ProductResponse(
    String sku,
    String description,
    String category,
    BigDecimal price,
    BigDecimal discountedPrice,
    int discountApplied) {}
