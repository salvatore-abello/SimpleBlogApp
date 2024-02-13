package it.salvatoreabello.simpleblogapp.controller;

import it.salvatoreabello.simpleblogapp.config.APIResponse;
import it.salvatoreabello.simpleblogapp.config.JWTUtil;
import it.salvatoreabello.simpleblogapp.dto.PostDTO;
import it.salvatoreabello.simpleblogapp.dto.UserDTO;
import it.salvatoreabello.simpleblogapp.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.OutputKeys;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private IPostService postService;

    @Autowired
    private JWTUtil jwt;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
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

    @GetMapping(value = {"/post/{id}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<PostDTO>> getPost(@PathVariable Integer id){
        APIResponse.APIResponseBuilder<PostDTO> builder = APIResponse.builder();

        PostDTO fetchedPost = postService.findById(id);

        builder.statusCode(HttpStatus.OK.value());

        if(fetchedPost != null){
            builder
                    .statusMessage("Post fetched correctly!")
                    .totalObjects(1)
                    .returnedObjects(1)
                    .payload(fetchedPost);
        }else{
            builder
                    .statusMessage("No user found with the given id")
                    .totalObjects(0)
                    .returnedObjects(0);
        }

        APIResponse<PostDTO> response = builder.build();


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // + owner?
    @GetMapping(value = {"/search"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<List<PostDTO>>> searchPosts(
    @RequestParam(value = "tags", required = false) List<String> tags,
    @RequestParam(value = "content", required = false) String content,
    @RequestParam(value = "title", required = false) String title,
    @RequestParam(value = "ownerid", required = false) Integer ownerId){
        APIResponse.APIResponseBuilder<List<PostDTO>> builder = APIResponse.builder();

        List<PostDTO> fetchedPosts = postService.searchPosts(tags, content, title, ownerId);
        builder.statusCode(HttpStatus.OK.value());

        if(!fetchedPosts.isEmpty()){
            builder
                    .statusMessage("Posts fetched correctly!")
                    .totalObjects(fetchedPosts.size())
                    .returnedObjects(fetchedPosts.size())
                    .payload(fetchedPosts);

        }else{
            builder
                    .statusMessage("No post found with the given filter(s)")
                    .totalObjects(0)
                    .returnedObjects(0);
        }

        APIResponse<List<PostDTO>> response = builder.build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = {"/create"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse<PostDTO>> createPost(@RequestBody @Validated PostDTO post) throws Exception {
        APIResponse.APIResponseBuilder<PostDTO> builder = APIResponse.builder();

        PostDTO createdPost = postService.saveOrUpdate(post);
        builder.statusCode(HttpStatus.OK.value());

        if(createdPost != null){
            builder
                    .statusCode(HttpStatus.OK.value())
                    .statusMessage("Post created")
                    .returnedObjects(1)
                    .totalObjects(1)
                    .payload(createdPost)
                    .build();
        }else{
            builder
                    .statusCode(HttpStatus.OK.value())
                    .statusMessage("Unable to create post")
                    .returnedObjects(0)
                    .totalObjects(0)
                    .build();
        }

        APIResponse<PostDTO> response = builder.build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
