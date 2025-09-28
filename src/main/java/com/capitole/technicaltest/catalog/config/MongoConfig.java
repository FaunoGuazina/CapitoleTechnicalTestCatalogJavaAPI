package com.capitole.technicaltest.catalog.config;

import java.math.BigDecimal;
import java.util.Arrays;
import org.bson.types.Decimal128;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.lang.Nullable;

@Configuration
public class MongoConfig {
  @Bean
  public MongoCustomConversions customConversions() {
    return new MongoCustomConversions(
        Arrays.asList(
            new BigDecimalToDecimal128Converter(), new Decimal128ToBigDecimalConverter()));
  }

  static class BigDecimalToDecimal128Converter implements Converter<BigDecimal, Decimal128> {
    @Override
    public Decimal128 convert(@Nullable BigDecimal source) {
      return (source != null ? new Decimal128(source) : Decimal128.POSITIVE_ZERO);
    }
  }

  static class Decimal128ToBigDecimalConverter implements Converter<Decimal128, BigDecimal> {
    @Override
    public BigDecimal convert(@Nullable Decimal128 source) {
      return (source != null ? source.bigDecimalValue() : BigDecimal.ZERO);
    }
  }
}
