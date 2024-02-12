package it.salvatoreabello.simpleblogapp.controller;

import it.salvatoreabello.simpleblogapp.config.APIResponse;
import it.salvatoreabello.simpleblogapp.dto.PostDTO;
import it.salvatoreabello.simpleblogapp.dto.UserDTO;
import it.salvatoreabello.simpleblogapp.service.IPostService;
import it.salvatoreabello.simpleblogapp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/api"})
public class Controller {
    @Autowired
    private IPostService postService;
    @Autowired
    private IUserService userService;

    @GetMapping(value = {"/posts"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<List<PostDTO>>> getAllPosts(){
        APIResponse.APIResponseBuilder<List<PostDTO>> builder = APIResponse.builder();
        List<PostDTO> posts = postService.getAll();
        builder.statusCode(HttpStatus.OK.value());

        if(!posts.isEmpty()){
            builder
                    .statusMessage("Posts fetched correctly!")
                    .totalObjects(posts.size())
                    .returnedObjects(posts.size())
                    .payload(posts);

        }else{
            builder
                    .statusMessage("No posts found")
                    .totalObjects(0)
                    .returnedObjects(0);
        }

        APIResponse<List<PostDTO>> response = builder.build();

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
