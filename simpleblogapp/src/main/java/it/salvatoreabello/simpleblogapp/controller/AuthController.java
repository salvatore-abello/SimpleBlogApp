package it.salvatoreabello.simpleblogapp.controller;

import it.salvatoreabello.simpleblogapp.config.APIError;
import it.salvatoreabello.simpleblogapp.config.APIResponse;
import it.salvatoreabello.simpleblogapp.config.ErrorDetail;
import it.salvatoreabello.simpleblogapp.config.JWTUtil;
import it.salvatoreabello.simpleblogapp.model.UserModel;
import it.salvatoreabello.simpleblogapp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    IUserService userService;
    @Autowired
    JWTUtil jwt;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<UserModel>> handleValidationErrors(MethodArgumentNotValidException ex) {
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
                APIResponse.<UserModel>builder()
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .statusMessage("Internal server error while registering user")
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

    @PostMapping(value = "/register",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<UserModel>> registerUser(@RequestBody @Validated UserModel entity){
        try {

            UserModel registeredUser = userService.saveOrUpdate(entity);
            System.out.println(jwt.createJWT(registeredUser));

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    APIResponse.<UserModel>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .statusMessage("User registered successfully")
                            .returnedObjects(1)
                            .totalObjects(1)
                            .payload(registeredUser).build()
            );
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    APIResponse.<UserModel>builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .statusMessage("Email already exists")
                            .returnedObjects(0)
                            .totalObjects(0)
                            .errors(
                                    APIError.builder()
                                            .path("/api/auth/register")
                                            .timestamp(new Date())
                                            .details(
                                                    Collections.singletonList(
                                                            ErrorDetail.builder()
                                                                    .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                                                                    .field("email")
                                                                    .source("repository.saveOrUpdate()")
                                                                    .message("Email already exists").build()
                                                    )
                                            ).build()
                            ).build()
            );
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    APIResponse.<UserModel>builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .statusMessage("Internal server error while registering user")
                            .returnedObjects(0)
                            .totalObjects(0)
                            .errors(
                                    APIError.builder()
                                            .path("/api/auth/register")
                                            .timestamp(new Date())
                                            .details(
                                                    Collections.singletonList(
                                                            ErrorDetail.builder()
                                                                    .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                                                                    .field("generic")
                                                                    .source("repository.saveOrUpdate()")
                                                                    .message(e.getMessage()).build()
                                                    )
                                            ).build()
                            ).build()
            );
        }
    }

    @PostMapping(value = "/login",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<String>> loginUser(@RequestBody Map<String, String> json){
        String email = json.get("email");
        APIResponse.APIResponseBuilder<String>builder = APIResponse.builder();
        try{
            System.out.println(json.get("password"));
            if(userService.login(email, json.get("password"))){
                builder
                        .statusCode(HttpStatus.OK.value())
                        .statusMessage("User logged successfully")
                        .returnedObjects(1)
                        .totalObjects(1)
                        .payload(jwt.createJWT(UserModel.builder()
                                .email(email)
                                .build())).build();
            }else{
                builder
                        .statusCode(HttpStatus.FORBIDDEN.value())
                        .statusMessage("Invalid credentials")
                        .returnedObjects(0)
                        .totalObjects(0).build();
            }

            APIResponse<String> response = builder.build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    APIResponse.<String>builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .statusMessage("Internal server error")
                            .returnedObjects(0)
                            .totalObjects(0)
                            .errors(
                                    APIError.builder()
                                            .timestamp(new Date())
                                            .path("/api/auth/login")
                                            .details(
                                                    Collections.singletonList(
                                                            ErrorDetail.builder()
                                                                    .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                                                                    .field("generic")
                                                                    .message(e.getMessage())
                                                                    .build()
                                                    )
                                            )
                                            .build()
                            ).build()

            );

        }

    }

}
