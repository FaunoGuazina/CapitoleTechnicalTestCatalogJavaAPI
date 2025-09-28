package com.capitole.technicaltest.catalog.adapters.inbound.rest.exception;

import static java.util.Objects.nonNull;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex) {
    return getProblemDetailResponse(
        HttpStatus.BAD_REQUEST,
        "Bad Request",
        ex.getMessage() != null ? ex.getMessage() : "Bad request",
        null);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ProblemDetail> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    return getProblemDetailResponse(
        HttpStatus.BAD_REQUEST, "Invalid Parameter", "Invalid parameter: " + ex.getName(), null);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleGeneric(Exception ex) {
    return getProblemDetailResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getClass().getSimpleName(),
        (ex.getMessage() != null ? ex.getMessage() : "Unknown error"),
        null);
  }

  private ResponseEntity<ProblemDetail> getProblemDetailResponse(
      HttpStatus status, String title, String detail, Map<String, String> errors) {
    var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
    problemDetail.setTitle(title);

    if (nonNull(errors)) {
      problemDetail.setProperty("errors", errors);
    }
    return ResponseEntity.status(status)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(problemDetail);
  }
}
