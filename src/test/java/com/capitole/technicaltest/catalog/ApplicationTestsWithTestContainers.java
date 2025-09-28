package com.capitole.technicaltest.catalog;

import org.springframework.boot.SpringApplication;

public class ApplicationTestsWithTestContainers {

  public static void main(String[] args) {
    SpringApplication.from(Application::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }
}
