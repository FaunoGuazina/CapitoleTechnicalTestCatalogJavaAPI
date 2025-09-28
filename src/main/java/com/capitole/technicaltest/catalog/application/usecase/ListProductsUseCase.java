package com.capitole.technicaltest.catalog.application.usecase;

import com.capitole.technicaltest.catalog.adapters.outbound.model.catalog.repository.ProductRepository;
import com.capitole.technicaltest.catalog.domain.mapper.ProductModelMapper;
import com.capitole.technicaltest.catalog.domain.model.ProductModel;
import com.capitole.technicaltest.catalog.domain.policy.DiscountPolicy;
import com.capitole.technicaltest.catalog.domain.ports.ProductModelRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListProductsUseCase implements ProductModelRepository {
  private final ProductRepository productRepository;
  private final ProductModelMapper mapper;

  @Override
  public Page<ProductModel> findAll(String category, Pageable pageable) {

    return (Optional.ofNullable(category)
            .map(c -> productRepository.findByCategoryContainingIgnoreCase(c, pageable))
            .orElseGet(() -> productRepository.findAll(pageable)))
        .map(mapper::toProductModel)
        .map(
            p -> {
              final int discount = DiscountPolicy.calculate(p.category(), p.sku());
              return p.toBuilder()
                  .discountApplied(discount)
                  .discountedPrice(applyDiscount(p.price(), discount))
                  .build();
            });
  }

  private BigDecimal applyDiscount(BigDecimal price, int pct) {
    if (pct <= 0) {
      return price.setScale(2, RoundingMode.HALF_UP);
    }
    BigDecimal hundred = BigDecimal.valueOf(100L);
    BigDecimal factor = hundred.subtract(BigDecimal.valueOf(pct));
    return price.multiply(factor).divide(hundred, 2, RoundingMode.HALF_UP);
  }
}
