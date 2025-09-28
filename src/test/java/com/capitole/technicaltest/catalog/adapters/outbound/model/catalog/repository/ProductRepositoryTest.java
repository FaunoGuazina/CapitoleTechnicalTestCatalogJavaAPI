package com.capitole.technicaltest.catalog.adapters.outbound.model.catalog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.capitole.technicaltest.catalog.adapters.outbound.model.catalog.entity.Product;
import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.utility.TestcontainersConfiguration;

@Slf4j
@DataMongoTest
@Import(TestcontainersConfiguration.class)
class ProductRepositoryTest {
  @Autowired private MongoTemplate mongoTemplate;

  @Autowired private ProductRepository repository;

  @BeforeEach
  void setup() {
    mongoTemplate.dropCollection(Product.class);
    // Insert test data
    List<Product> testProducts =
        List.of(
            new Product(null, "SKU001", new BigDecimal("1000.00"), "Laptop", "Electronics"),
            new Product(null, "SKU002", new BigDecimal("200.00"), "Chair", "Home & Kitchen"),
            new Product(null, "SKU005", new BigDecimal("50.00"), "Mouse", "Electronics"));
    mongoTemplate.insertAll(testProducts);
  }

  @Test
  void shouldFindProductsByCategory() {
    var result = repository.findByCategoryContainingIgnoreCase("Electronics", Pageable.unpaged());

    assertThat(result.getContent()).hasSize(2);
    assertThat(
            result.getContent().stream()
                .map(Product::category)
                .allMatch(c -> c.equals("Electronics")))
        .isTrue();
  }

  @Test
  void shouldReturnAllProductsWhenCategoryIsNull() {
    var result = repository.findAll(Pageable.unpaged());

    assertThat(result.getContent()).hasSize(3);
  }

  @Test
  void shouldSupportPaginationCorrectly() {
    var result = repository.findAll(PageRequest.of(0, 2));

    assertThat(result.getContent()).hasSize(2);
    assertThat(result.getTotalElements()).isEqualTo(3);
    assertThat(result.getTotalPages()).isEqualTo(2);
  }

  @Test
  void shouldSupportSortingByPriceAscending() {
    var sort = Sort.by(Sort.Direction.DESC, "price");
    var pageable = PageRequest.of(0, 10, sort);
    var result = repository.findAll(pageable);

    assertThat(result.getContent()).hasSize(3);
    var prices = result.getContent().stream().map(Product::price).toList();
    log.info("ASC Prices: {}", prices);
    assertThat(prices)
        .containsExactly(
            new BigDecimal("50.00"), new BigDecimal("200.00"), new BigDecimal("1000.00"));
  }

  @Test
  void shouldSupportSortingByPriceDescending() {
    var sort = Sort.by(Sort.Direction.ASC, "price");
    var pageable = PageRequest.of(0, 10, sort);
    var result = repository.findAll(pageable);

    assertThat(result.getContent()).hasSize(3);
    var prices = result.getContent().stream().map(Product::price).toList();
    log.info("DESC Prices: {}", prices);
    assertThat(prices)
        .containsExactly(
            new BigDecimal("1000.00"), new BigDecimal("200.00"), new BigDecimal("50.00"));
  }
}
