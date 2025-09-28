package com.capitole.technicaltest.catalog.domain.policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DiscountPolicy {
  public static int calculate(String category, String sku) {
    List<Integer> discounts = new ArrayList<>();
    if (category.equalsIgnoreCase("Home & Kitchen")) discounts.add(25);
    if (category.equalsIgnoreCase("Electronics")) discounts.add(15);
    if (sku.endsWith("5")) discounts.add(30);
    return discounts.isEmpty() ? 0 : Collections.max(discounts);
  }
}
