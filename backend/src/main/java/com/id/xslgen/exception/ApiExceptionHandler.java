package com.id.xslgen.exception;

import com.id.xslgen.dto.ApiErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ResponseEntity<ApiErrorDto> notFound(Exception e) {
        return ResponseEntity.status(404).body(new com.id.xslgen.dto.ApiErrorDto("NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<com.id.xslgen.dto.ApiErrorDto> generic(Exception e) {
        return ResponseEntity.status(500).body(new com.id.xslgen.dto.ApiErrorDto("INTERNAL", "Unexpected error"));
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiErrorDto> forbidden(Exception e) {
        return ResponseEntity.status(403).body(new ApiErrorDto("FORBIDDEN", "No access"));
    }
}
