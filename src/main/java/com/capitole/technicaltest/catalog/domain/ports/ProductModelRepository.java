package com.capitole.technicaltest.catalog.domain.ports;

import com.capitole.technicaltest.catalog.domain.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

public interface ProductModelRepository {
  Page<ProductModel> findAll(@Nullable String category, Pageable pageable);
}
