package it.salvatoreabello.simpleblogapp.controller;

import it.salvatoreabello.simpleblogapp.config.APIError;
import it.salvatoreabello.simpleblogapp.config.APIResponse;
import it.salvatoreabello.simpleblogapp.config.ErrorDetail;
import it.salvatoreabello.simpleblogapp.model.PostModel;
import it.salvatoreabello.simpleblogapp.model.UserModel;
import it.salvatoreabello.simpleblogapp.service.IPostService;
import it.salvatoreabello.simpleblogapp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = {"/api/"})
public class Controller {
    @Autowired
    private IPostService postService;
    @Autowired
    private IUserService userService;

    @GetMapping(value = {"/posts"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<List<PostModel>>> getAllPosts(){
        APIResponse.APIResponseBuilder<List<PostModel>> builder = APIResponse.builder();
        try{
            List<PostModel> posts = postService.getAll();
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

            APIResponse<List<PostModel>> response = builder.build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    APIResponse.<List<PostModel>>builder()
                            .statusMessage("Internal server error while fetching posts")
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .totalObjects(0)
                            .returnedObjects(0)
                            .errors(
                                    APIError.builder()
                                            .timestamp(new Date())
                                            .path("/api/posts")
                                            .details(
                                                    Collections.singletonList(
                                                            ErrorDetail.builder()
                                                                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                                                                    .field("generic")
                                                                    .source("repository.getAll()")
                                                                    .message(e.getMessage()).build()

                                                    )
                                            ).build()
                            ).build()
            );
        }
    }
    @GetMapping(value = {"/user/{id}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<UserModel>> getUser(@PathVariable Integer id){
        APIResponse.APIResponseBuilder<UserModel> builder = APIResponse.builder();
        try{
            UserModel fetchedUser = userService.findById(id);

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

            APIResponse<UserModel> response = builder.build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    APIResponse.<UserModel>builder()
                            .statusMessage("Internal server error while fetching user")
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .totalObjects(0)
                            .returnedObjects(0)
                            .errors(
                                    APIError.builder()
                                            .timestamp(new Date())
                                            .path("/api/user/" + id)
                                            .details(
                                                    Collections.singletonList(
                                                            ErrorDetail.builder()
                                                                    .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                                                                    .field("generic")
                                                                    .source("repository.findById()")
                                                                    .message(e.getMessage()).build()

                                                    )
                                            ).build()
                            ).build()
            );
        }
    }

}
