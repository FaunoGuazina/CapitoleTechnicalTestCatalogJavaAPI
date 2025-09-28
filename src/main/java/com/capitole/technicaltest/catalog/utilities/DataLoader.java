package com.capitole.technicaltest.catalog.utilities;

import com.capitole.technicaltest.catalog.adapters.outbound.model.catalog.entity.Product;
import com.capitole.technicaltest.catalog.adapters.outbound.model.catalog.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
  private final ProductRepository productRepository;

  @Override
  public void run(String... args) {
    if (productRepository.count() == 0L) {
      loadInitialData();
    }
  }

  private void loadInitialData() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      List<Product> products =
          mapper.readValue(
              new ClassPathResource("data/products.json").getInputStream(),
              new TypeReference<>() {});

      List<Product> documents = products.stream().toList();

      productRepository.saveAll(documents);

      log.info("Loaded {} products into MongoDB", products.size());
    } catch (Exception e) {
      log.info("Error loading initial data: {}", e.getMessage());
    }
  }
}
