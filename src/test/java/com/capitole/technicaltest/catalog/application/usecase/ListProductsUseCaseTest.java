package com.capitole.technicaltest.catalog.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.capitole.technicaltest.catalog.adapters.outbound.model.catalog.entity.Product;
import com.capitole.technicaltest.catalog.adapters.outbound.model.catalog.repository.ProductRepository;
import com.capitole.technicaltest.catalog.domain.mapper.ProductModelMapper;
import com.capitole.technicaltest.catalog.domain.model.ProductModel;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ListProductsUseCaseTest {
  @Mock private ProductRepository repository;
  @Mock private ProductModelMapper mapper;
  @InjectMocks private ListProductsUseCase useCase;

  @BeforeEach
  void setupMapper() {
    given(mapper.toProductModel(any()))
        .willAnswer(
            inv -> {
              var p = (Product) inv.getArgument(0);
              return ProductModel.builder()
                  .sku(p.sku())
                  .price(p.price())
                  .description(p.description())
                  .category(p.category())
                  .build();
            });
  }

  @Test
  void shouldApplyCorrectDiscountToProducts() {
    // Given
    List<Product> products =
        Arrays.asList(
            new Product(null, "SKU001", new BigDecimal("1000.00"), "Laptop", "Electronics"),
            new Product(null, "SKU005", new BigDecimal("50.00"), "Mouse", "Electronics"));
    PageImpl<Product> page = new PageImpl<>(products);
    given(repository.findAll(Pageable.unpaged())).willReturn(page);
    // When
    var result = useCase.findAll(null, Pageable.unpaged());

    // Then
    assertThat(result.getContent()).hasSize(2);
    assertThat(result.getContent().get(0).discountApplied()).isEqualTo(15); // Electronics
    assertThat(result.getContent().get(1).discountApplied()).isEqualTo(30); // SKU ends with 5
  }

  @Test
  void shouldFilterByCategoryWhenProvided() {
    // Given
    List<Product> electronics =
        List.of(new Product(null, "SKU001", new BigDecimal("1000.00"), "Laptop", "Electronics"));
    PageImpl<Product> page = new PageImpl<>(electronics);
    given(repository.findByCategoryContainingIgnoreCase("Electronics", Pageable.unpaged()))
        .willReturn(page);

    // When
    var result = useCase.findAll("Electronics", Pageable.unpaged());

    // Then
    verify(repository).findByCategoryContainingIgnoreCase("Electronics", Pageable.unpaged());
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().getFirst().category()).isEqualTo("Electronics");
  }
}
