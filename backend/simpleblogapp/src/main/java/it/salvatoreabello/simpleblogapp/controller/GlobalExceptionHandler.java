package it.salvatoreabello.simpleblogapp.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import it.salvatoreabello.simpleblogapp.config.APIError;
import it.salvatoreabello.simpleblogapp.config.APIResponse;
import it.salvatoreabello.simpleblogapp.config.ErrorDetail;
import it.salvatoreabello.simpleblogapp.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ExpiredJwtException.class, MalformedJwtException.class}) // JWT exceptions
    protected ResponseEntity<APIResponse<UserDTO>> expiredHandler(HttpServletRequest request, Exception ex){
        StackTraceElement[] stackTrace = ex.getStackTrace();

        ex.printStackTrace(); // remove this

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                APIResponse.<UserDTO>builder()
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .statusMessage("Internal server error")
                        .returnedObjects(0)
                        .totalObjects(0)
                        .errors(
                                APIError.builder()
                                        .path(request.getServletPath())
                                        .timestamp(new Date())
                                        .details(
                                                Collections.singletonList(
                                                        ErrorDetail.builder()
                                                                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                                                                .field("jwt")
                                                                .source(stackTrace[0].getMethodName())
                                                                .message("Malformed or expired JWT")
                                                                .build()
                                                )
                                        ).build()
                        ).build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // Field validation
    public ResponseEntity<APIResponse<UserDTO>> handleConflict(MethodArgumentNotValidException ex) {
        List<ErrorDetail> errorDetails = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> ErrorDetail.builder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .source("repository.saveOrUpdate")
                        .build())
                .toList();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                APIResponse.<UserDTO>builder()
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .statusMessage("Internal server error")
                        .returnedObjects(0)
                        .totalObjects(0)
                        .errors(
                                APIError.builder()
                                        .path("/api/auth/register")
                                        .timestamp(new Date())
                                        .details(errorDetails).build()
                        ).build()
        );
    }

    @ExceptionHandler(Exception.class) // Generic handler
    protected ResponseEntity<APIResponse<Object>> genericExceptionHandler(HttpServletRequest request, Exception ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();

        ex.printStackTrace(); // remove this
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                APIResponse.<Object>builder()
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .statusMessage("Internal server error")
                        .returnedObjects(0)
                        .totalObjects(0)
                        .errors(
                                APIError.builder()
                                        .path(request.getServletPath())
                                        .timestamp(new Date())
                                        .details(
                                                Collections.singletonList(
                                                        ErrorDetail.builder()
                                                                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                                                                .field("generic")
                                                                .source(stackTrace[0].getMethodName())
                                                                .message(ex.getMessage())
                                                                .build()
                                                )
                                        ).build()
                        ).build()
        );
    }

}
