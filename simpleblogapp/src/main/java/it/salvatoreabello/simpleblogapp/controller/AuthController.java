package it.salvatoreabello.simpleblogapp.controller;

import it.salvatoreabello.simpleblogapp.config.APIError;
import it.salvatoreabello.simpleblogapp.config.APIResponse;
import it.salvatoreabello.simpleblogapp.config.ErrorDetail;
import it.salvatoreabello.simpleblogapp.model.UserModel;
import it.salvatoreabello.simpleblogapp.service.IUserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    IUserService userService;

    @PostMapping(value = "/register",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<UserModel>> registerUser(@RequestBody @Validated UserModel entity){
        try {
            UserModel registeredUser = userService.saveOrUpdate(entity);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    APIResponse.<UserModel>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .statusMessage("User registered successfully")
                            .returnedObjects(1)
                            .totalObjects(1)
                            .payload(registeredUser).build()
            );
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    APIResponse.<UserModel>builder()
                            .statusCode(HttpStatus.CONFLICT.value())
                            .statusMessage("Email already exists")
                            .returnedObjects(0)
                            .totalObjects(0).build()
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
}
