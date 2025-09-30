package com.capitole.technicaltest.catalog.domain.policy;

import com.capitole.technicaltest.catalog.domain.model.ProductModel;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscountEngine {
  private final DiscountSettings settings;

  public int calculate(ProductModel p) {
    return settings.rules().stream()
        .map(rule -> applies(rule, p) ? rule.pct() : 0)
        .max(Integer::compareTo)
        .orElse(0);
  }

  private boolean applies(DiscountSettings.Rule r, ProductModel p) {
    String type = StringUtils.defaultString(r.type()).toLowerCase(Locale.ROOT);
    String match = StringUtils.defaultString(r.match()).trim();

    return switch (type) {
      case "category" -> equalsIgnoreCaseSafe(p.category(), match);
      case "skusuffix" -> endsWithSafe(p.sku(), match);
      default -> false;
    };
  }

  private boolean equalsIgnoreCaseSafe(String a, String b) {
    return StringUtils.isNotBlank(a) && a.trim().equalsIgnoreCase(b);
  }

  private boolean endsWithSafe(String text, String suffix) {
    return StringUtils.isNotBlank(text)
        && StringUtils.isNotBlank(suffix)
        && text.trim().endsWith(suffix);
  }
}
