package it.salvatoreabello.simpleblogapp.controller;

import it.salvatoreabello.simpleblogapp.config.APIError;
import it.salvatoreabello.simpleblogapp.config.APIResponse;
import it.salvatoreabello.simpleblogapp.config.ErrorDetail;
import it.salvatoreabello.simpleblogapp.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<APIResponse<UserDTO>> handleConflict(Exception ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();

        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
        URI newUri = builder.build().toUri();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                APIResponse.<UserDTO>builder()
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .statusMessage("Internal server error while registering user")
                        .returnedObjects(0)
                        .totalObjects(0)
                        .errors(
                                APIError.builder()
                                        .path(newUri.getPath())
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
