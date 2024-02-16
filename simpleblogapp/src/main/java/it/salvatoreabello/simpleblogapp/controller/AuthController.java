package it.salvatoreabello.simpleblogapp.controller;

import it.salvatoreabello.simpleblogapp.config.APIError;
import it.salvatoreabello.simpleblogapp.config.APIResponse;
import it.salvatoreabello.simpleblogapp.config.ErrorDetail;
import it.salvatoreabello.simpleblogapp.config.JWTUtil;
import it.salvatoreabello.simpleblogapp.dto.UserDTO;
import it.salvatoreabello.simpleblogapp.model.UserModel;
import it.salvatoreabello.simpleblogapp.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    IUserService userService;
    @Autowired
    JWTUtil jwt;

    /*@GetMapping(value = "/csrf",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<String>> getCsrfToken(HttpServletRequest request){
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        return ResponseEntity.status(HttpStatus.OK).body(
                APIResponse.<String>builder()
                        .statusCode(HttpStatus.OK.value())
                        .statusMessage("CSRF token fetched correctly")
                        .returnedObjects(1)
                        .totalObjects(1)
                        .payload(csrfToken.getToken())
                        .build()
        );
    }*/

    @PostMapping(value = "/register",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<UserDTO>> registerUser(@RequestBody @Validated UserModel entity) throws MethodArgumentNotValidException {
        try {
            UserDTO registeredUser = userService.saveOrUpdate(entity);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    APIResponse.<UserDTO>builder()
                            .statusCode(HttpStatus.CREATED.value())
                            .statusMessage("User registered successfully")
                            .returnedObjects(1)
                            .totalObjects(1)
                            .payload(registeredUser).build()
            );
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    APIResponse.<UserDTO>builder()
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
        }
    }

    @PostMapping(value = "/login",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<String>> loginUser(@RequestBody Map<String, Object> json){
        APIResponse.APIResponseBuilder<String>builder = APIResponse.builder();

        String email = (String) json.get("email");
        String password = (String) json.get("password");

        UserModel fromDb = userService.findByEmail(email);

        UserModel fromReq = UserModel.builder()
                .email(email)
                .password(password)
                .build();

        if(fromDb == null) {
            builder
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .statusMessage("No user found with the given email")
                    .returnedObjects(0)
                    .totalObjects(0).build();
        }else if(userService.login(fromReq, fromDb)){
            builder
                    .statusCode(HttpStatus.OK.value())
                    .statusMessage("User logged successfully")
                    .returnedObjects(1)
                    .totalObjects(1)
                    .payload(jwt.createJWT(UserModel.builder()
                            .id(fromDb.getId())
                                    .name(fromDb.getName())
                                    .surname(fromDb.getSurname())
                                    .email(fromDb.getEmail()).build())).build();
        }else{
            builder
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .statusMessage("Invalid credentials")
                    .returnedObjects(0)
                    .totalObjects(0).build();
        }

        APIResponse<String> response = builder.build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/changepassword",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<String>> changePassword(@RequestBody Map<String, Object> json) throws Exception {
        APIResponse.APIResponseBuilder<String>builder = APIResponse.builder();

        String newPassword = (String) json.get("newPassword");
        String currentPassword = (String) json.get("currentPassword");

        userService.changePassword(currentPassword, newPassword);

        builder
                .statusCode(HttpStatus.OK.value())
                .statusMessage("Password changed correctly")
                .returnedObjects(1)
                .totalObjects(1)
                .payload("Password changed correctly");

        APIResponse<String> response = builder.build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}