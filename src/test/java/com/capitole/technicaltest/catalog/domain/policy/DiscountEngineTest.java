package com.capitole.technicaltest.catalog.domain.policy;

import static org.assertj.core.api.Assertions.assertThat;

import com.capitole.technicaltest.catalog.domain.model.ProductModel;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DiscountEngineTest {

  private DiscountEngine engine;

  @BeforeEach
  void setUp() {
    DiscountSettings settings =
        new DiscountSettings(
            List.of(
                new DiscountSettings.Rule("category", "Electronics", 15),
                new DiscountSettings.Rule("category", "Home & Kitchen", 25),
                new DiscountSettings.Rule("skuSuffix", "5", 30)));
    engine = new DiscountEngine(settings);
  }

  @Test
  void electronicsGets15() {
    var p = product("SKU0001", "Electronics", "10.00");
    assertThat(engine.calculate(p)).isEqualTo(15);
  }

  @Test
  void homeAndKitchenGets25() {
    var p = product("SKU0010", "Home & Kitchen", "100.00");
    assertThat(engine.calculate(p)).isEqualTo(25);
  }

  @Test
  void skuEndingWith5Gets30() {
    var p = product("SKU0015", "Footwear", "100.00");
    assertThat(engine.calculate(p)).isEqualTo(30);
  }

  @Test
  void maxDiscountWins_categoryVsSkuSuffix() {
    var p = product("SKU0015", "Electronics", "100.00");
    assertThat(engine.calculate(p)).isEqualTo(30);
  }

  @Test
  void unknownCategoryGets0() {
    var p = product("SKU0002", "Stationery", "100.00");
    assertThat(engine.calculate(p)).isZero();
  }

  @Test
  void nullSafety_onCategoryAndSku() {
    var p1 = product(null, "Electronics", "100.00");
    var p2 = product("SKU0002", null, "100.00");
    var p3 = product(null, null, "100.00");

    assertThat(engine.calculate(p1)).isEqualTo(15);
    assertThat(engine.calculate(p2)).isZero();
    assertThat(engine.calculate(p3)).isZero();
  }

  // helper
  private ProductModel product(String sku, String category, String price) {
    return ProductModel.builder()
        .sku(sku)
        .category(category)
        .price(new BigDecimal(price))
        .description("x")
        .discountApplied(0)
        .discountedPrice(null)
        .build();
  }
}
