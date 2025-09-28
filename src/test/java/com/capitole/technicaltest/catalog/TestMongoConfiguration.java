package com.capitole.technicaltest.catalog;

import com.capitole.technicaltest.catalog.config.MongoConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(MongoConfig.class)
public class TestMongoConfiguration {}
