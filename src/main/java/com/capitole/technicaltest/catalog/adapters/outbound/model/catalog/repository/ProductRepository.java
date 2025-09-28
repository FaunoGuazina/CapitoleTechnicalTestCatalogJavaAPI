package com.capitole.technicaltest.catalog.adapters.outbound.model.catalog.repository;

import com.capitole.technicaltest.catalog.adapters.outbound.model.catalog.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
  Page<Product> findByCategoryContainingIgnoreCase(String category, Pageable pageable);
}
