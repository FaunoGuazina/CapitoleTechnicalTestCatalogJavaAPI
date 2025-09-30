package com.capitole.technicaltest.catalog.domain.policy;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "discounts")
public record DiscountSettings(List<Rule> rules) {
  public record Rule(String type, String match, int pct) {}
}
