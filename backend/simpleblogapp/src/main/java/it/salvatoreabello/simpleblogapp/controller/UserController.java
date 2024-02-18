package it.salvatoreabello.simpleblogapp.controller;

import it.salvatoreabello.simpleblogapp.config.APIResponse;
import it.salvatoreabello.simpleblogapp.dto.PostDTO;
import it.salvatoreabello.simpleblogapp.dto.UserDTO;
import it.salvatoreabello.simpleblogapp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = {"/api/users"})
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<List<UserDTO>>> getAllPosts(){
        APIResponse.APIResponseBuilder<List<UserDTO>> builder = APIResponse.builder();

        List<UserDTO> users = userService.getAll();
        builder.statusCode(HttpStatus.OK.value());

        if(!users.isEmpty()){
            builder
                    .statusCode(HttpStatus.OK.value())
                    .statusMessage("Users fetched correctly")
                    .totalObjects(users.size())
                    .returnedObjects(users.size())
                    .payload(users);
        }else{
            builder
                    .statusCode(HttpStatus.OK.value())
                    .statusMessage("No users found")
                    .totalObjects(0)
                    .returnedObjects(0);
        }

        APIResponse<List<UserDTO>> response = builder.build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = {"/user/{id}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<UserDTO>> getUser(@PathVariable Integer id){
        APIResponse.APIResponseBuilder<UserDTO> builder = APIResponse.builder();
        UserDTO fetchedUser = userService.findById(id);

        builder.statusCode(HttpStatus.OK.value());
        if(fetchedUser != null){
            builder
                    .statusMessage("User fetched correctly")
                    .returnedObjects(1)
                    .totalObjects(1)
                    .payload(fetchedUser);
        }else{
            builder
                    .statusMessage("No user found with the given id")
                    .returnedObjects(0)
                    .totalObjects(0);
        }

        APIResponse<UserDTO> response = builder.build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
