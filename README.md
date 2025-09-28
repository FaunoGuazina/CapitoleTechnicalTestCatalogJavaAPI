# Product Catalog API · Java + Spring Boot

REST API for product catalog with **category filter**,
**pagination**, **sorting**, and **discount policy**.
**Hexagonal** architecture with **MapStruct**, **Spring Data MongoDB**, and
**OpenAPI**.

------------------------------------------------------------------------

## ✅ Covered Requirements

- `GET /products` with:
    - Optional filter `category`
    - Pagination `page` and `size`
    - Sorting by `sku`, `price`, `description`, `category`
- Discounts applied by:
    - Category `Electronics` → 15%
    - Category `Home & Kitchen` → 25%
    - SKU ending in `5` → 30%
    - Highest discount wins
- Typed responses with DTO
- Clean layered architecture

> Existing implementation notes: controller with HATEOAS
> PagedModel, REST mapper with MapStruct, discount policy, and
> Decimal128↔BigDecimal converters for Mongo.

------------------------------------------------------------------------

## 🏗️ Architecture

    adapters/
      inbound/rest/            # Controllers + DTO + mappers
      outbound/model/catalog   # Mongo repos + entities
    application/
      usecase                  # Use cases
    domain/
      model + policy + ports   # Logic and contracts
    config/
      MongoConfig, OpenApiConfig
    utilities/
      DataLoader               # Initial seed (products.json)

- Controller: `ProductController` returns
  `PagedModel<EntityModel<ProductResponse>>`\
- Use case: `ListProductsUseCase` applies discount and calculates
  `discountedPrice`\
- Policy: `DiscountPolicy.calculate(category, sku)`\
- Persistence: `ProductRepository` with
  `findByCategoryContainingIgnoreCase(...)`\
- Mongo conversion Decimal128↔BigDecimal: `MongoConfig`\
- Error handling: `RestExceptionHandler` with RFC7807
  (`ProblemDetail`)

------------------------------------------------------------------------

## 🛠️ Stack

- Java 21
- Spring Boot 3.5.6
- Spring Web, Spring HATEOAS, Spring Data MongoDB
- MapStruct
- OpenAPI 3 + springdoc
- JUnit 5

------------------------------------------------------------------------

## ▶️ Local Execution

### Prerequisites

- Docker & Docker Compose installed
- JDK 21 (if running locally without Docker)

### Run with Docker

```bash
docker compose up --build
```

This starts:

- Swagger UI at [http://localhost:8080/api-docs.html](http://localhost:8080/api-docs.html)

------------------------------------------------------------------------

---

## 🔍 API Endpoints

### List Products

`GET /products`

#### Query Parameters

- `category` → filter by category (e.g. Electronics, Home & Kitchen).
- `page` & `size` → pagination.
- `sort` → field and direction, e.g. `sort=price,desc`.

#### Example

```http
GET /products?category=Electronics&sort=price,asc&page=0&size=5
```

#### Sample Response

```json
[
  {
    "sku": "SKU0001",
    "description": "Wireless Mouse with ergonomic design",
    "category": "Electronics",
    "price": 19.99,
    "discountedPrice": 16.99
  }
]
```

---

## 📂 Initial Data

The API loads products from [`resources/data/products.json`](src/main/resources/data/products.json):  
Examples include Electronics, Home & Kitchen, Clothing, Accessories, etc.

---

------------------------------------------------------------------------

## 🧪 Testing

### Run Unit and Integration Tests

- **Unit tests** cover discount policies, filtering, and sorting.
- **Integration tests** boot the Spring context and validate `/products` with Testcontainers.

---

## 🐳 Docker Setup

### Multi-stage Dockerfile

Builds and packages the application efficiently.

### docker-compose.yml

- Runs API and MongoDB containers.

---

## 📖 Documentation

- Swagger UI → [http://localhost:8080/api-docs.html](http://localhost:8080/api-docs.html)
- OpenAPI JSON → [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---