package com.capitole.technicaltest.catalog.adapters.inbound.rest;

import static org.assertj.core.api.Assertions.assertThat;

import com.capitole.technicaltest.catalog.adapters.outbound.model.catalog.entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.utility.TestcontainersConfiguration;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class ProductControllerTest {

  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired private MongoTemplate mongoTemplate;

  @BeforeEach
  void setup() {
    mongoTemplate.dropCollection(Product.class);
    List<Product> testProducts =
        List.of(
            new Product(null, "SKU001", new BigDecimal("1500.00"), "Dell Laptop", "Electronics"),
            new Product(
                null, "SKU002", new BigDecimal("300.00"), "Ergonomic Chair", "Home & Kitchen"),
            new Product(null, "SKU005", new BigDecimal("75.00"), "Gaming Mouse", "Electronics"),
            new Product(null, "SKU003", new BigDecimal("25.00"), "T-shirt", "Clothing"));
    mongoTemplate.insertAll(testProducts);
  }

  @Test
  void getProductsShouldReturnListWithAppliedDiscounts() throws Exception {
    // When
    ResponseEntity<String> response = testRestTemplate.getForEntity("/products", String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    ObjectMapper mapper = new ObjectMapper();
    var jsonResponse = mapper.readTree(response.getBody());
    var products = jsonResponse.get("_embedded").get("content");

    assertThat(products).hasSize(4);

    // Verify specific discounts
    var electronicsProduct =
        java.util.stream.StreamSupport.stream(products.spliterator(), false)
            .filter(
                it ->
                    it.get("category").asText().equals("Electronics")
                        && !it.get("sku").asText().endsWith("5"))
            .findFirst()
            .orElse(null);
    assertThat(
            electronicsProduct != null ? electronicsProduct.get("discountApplied").asInt() : null)
        .isEqualTo(15);

    var skuEnding5 =
        java.util.stream.StreamSupport.stream(products.spliterator(), false)
            .filter(it -> it.get("sku").asText().endsWith("5"))
            .findFirst()
            .orElse(null);
    assertThat(skuEnding5 != null ? skuEnding5.get("discountApplied").asInt() : null).isEqualTo(30);

    var homeKitchen =
        java.util.stream.StreamSupport.stream(products.spliterator(), false)
            .filter(it -> it.get("category").asText().equals("Home & Kitchen"))
            .findFirst()
            .orElse(null);
    assertThat(homeKitchen != null ? homeKitchen.get("discountApplied").asInt() : null)
        .isEqualTo(25);
  }

  @Test
  void getProductsWithCategoryFilterShouldReturnOnlyProductsOfThatCategory() throws Exception {
    // When
    ResponseEntity<String> response =
        testRestTemplate.getForEntity("/products?category=Electronics", String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    ObjectMapper mapper = new ObjectMapper();
    var jsonResponse = mapper.readTree(response.getBody());
    var products = jsonResponse.get("_embedded").get("content");

    assertThat(products).hasSize(2);
    java.util.stream.StreamSupport.stream(products.spliterator(), false)
        .forEach(product -> assertThat(product.get("category").asText()).isEqualTo("Electronics"));
  }

  @Test
  void getProductsWithPaginationShouldWorkCorrectly() throws Exception {
    // When
    ResponseEntity<String> response =
        testRestTemplate.getForEntity("/products?page=0&size=2", String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    ObjectMapper mapper = new ObjectMapper();
    var jsonResponse = mapper.readTree(response.getBody());
    assertThat(jsonResponse.get("_embedded").get("content")).hasSize(2);
    assertThat(jsonResponse.get("page").get("totalElements").asInt()).isEqualTo(4);
    assertThat(jsonResponse.get("page").get("totalPages").asInt()).isEqualTo(2);
  }

  @Test
  void getProductsWithSortingShouldWorkCorrectly() throws Exception {
    // When
    ResponseEntity<String> response =
        testRestTemplate.getForEntity("/products?sort=price,desc", String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    ObjectMapper mapper = new ObjectMapper();
    var jsonResponse = mapper.readTree(response.getBody());
    log.info("Response JSON: {}", jsonResponse);
    var products = jsonResponse.get("_embedded").get("content");
    java.util.List<Double> prices = new java.util.ArrayList<>();
    java.util.stream.StreamSupport.stream(products.spliterator(), false)
        .forEach(it -> prices.add(it.get("price").asDouble()));
    assertThat(prices).isSortedAccordingTo((o1, o2) -> Double.compare(o2, o1));
  }
}
