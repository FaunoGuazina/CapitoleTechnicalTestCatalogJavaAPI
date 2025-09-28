package com.capitole.technicaltest.catalog.adapters.inbound.rest;

import com.capitole.technicaltest.catalog.adapters.inbound.rest.dto.ProductResponse;
import com.capitole.technicaltest.catalog.adapters.inbound.rest.mapper.ProductResponseMapper;
import com.capitole.technicaltest.catalog.application.usecase.ListProductsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@Tag(
    name = "Product Management",
    description =
        "API for managing product catalog operations including listing and filtering products")
@RequiredArgsConstructor
public class ProductController {
  private final ListProductsUseCase listProductsUseCase;
  private final ProductResponseMapper mapper;
  private final PagedResourcesAssembler<ProductResponse> pagedResourcesAssembler;

  @GetMapping
  @Operation(
      summary = "List products",
      description =
          "Retrieve a paginated list of products with optional category filtering. "
              + "Returns products sorted by default criteria with pagination support.",
      operationId = "listProducts")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Products retrieved successfully",
            content =
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PagedModel.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters",
            content = @Content()),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content())
      })
  public PagedModel<EntityModel<ProductResponse>> getProducts(
      @Parameter(
              name = "category",
              description =
                  "Optional category filter to retrieve products from a specific category. "
                      + "If not provided, all products will be returned.",
              example = "electronics")
          @RequestParam(required = false)
          String category,
      @Parameter(
              description =
                  "Pagination parameters including page number, size, and sorting options. Default sort: `sku,ASC`")
          @ParameterObject
          @PageableDefault(sort = "sku", direction = Sort.Direction.ASC)
          Pageable pageable) {
    return pagedResourcesAssembler.toModel(
        listProductsUseCase.findAll(category, pageable).map(mapper::toProductResponse));
  }
}
