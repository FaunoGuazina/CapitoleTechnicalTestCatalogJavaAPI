package com.capitole.technicaltest.catalog.domain.mapper;

import com.capitole.technicaltest.catalog.adapters.outbound.model.catalog.entity.Product;
import com.capitole.technicaltest.catalog.domain.model.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductModelMapper {
  ProductModel toProductModel(Product product);
}
