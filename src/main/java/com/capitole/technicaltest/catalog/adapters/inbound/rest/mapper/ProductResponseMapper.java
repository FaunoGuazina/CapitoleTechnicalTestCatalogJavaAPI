package com.capitole.technicaltest.catalog.adapters.inbound.rest.mapper;

import com.capitole.technicaltest.catalog.adapters.inbound.rest.dto.ProductResponse;
import com.capitole.technicaltest.catalog.domain.model.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductResponseMapper {
  ProductResponse toProductResponse(ProductModel product);
}
