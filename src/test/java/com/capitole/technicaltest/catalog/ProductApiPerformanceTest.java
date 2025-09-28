package com.capitole.technicaltest.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import com.capitole.technicaltest.catalog.adapters.outbound.model.catalog.entity.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class ProductApiPerformanceTest {
  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired private MongoTemplate mongoTemplate;

  @Test
  void apiShouldRespondInLessThan500msWith1000Products() {
    // Given - insert 1000 products in the DB
    List<Product> manyProducts =
        IntStream.rangeClosed(1, 1000)
            .mapToObj(
                i ->
                    new Product(
                        "SKU" + i, "Product " + i, new BigDecimal("100.00"), "Test", "Electronics"))
            .collect(Collectors.toList());
    mongoTemplate.insertAll(manyProducts);

    // When
    long startTime = System.currentTimeMillis();
    var response = testRestTemplate.getForEntity("/products", String.class);
    long endTime = System.currentTimeMillis();

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(endTime - startTime).isLessThan(500); // less than 500ms
  }
}
