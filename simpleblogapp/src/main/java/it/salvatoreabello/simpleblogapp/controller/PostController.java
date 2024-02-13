package it.salvatoreabello.simpleblogapp.controller;

import it.salvatoreabello.simpleblogapp.config.APIResponse;
import it.salvatoreabello.simpleblogapp.dto.PostDTO;
import it.salvatoreabello.simpleblogapp.dto.TagDTO;
import it.salvatoreabello.simpleblogapp.service.IPostService;
import it.salvatoreabello.simpleblogapp.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private ITagService tagService;

    @Autowired
    private IPostService postService;

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

    @GetMapping(value = {"/tags"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<List<TagDTO>>> searchPostsByTags(@RequestParam("tags") List<String> tags){
        APIResponse.APIResponseBuilder<List<TagDTO>> builder = APIResponse.builder();

        List<TagDTO> fetchedPosts = tagService.findByTagnameIn(tags);
        builder.statusCode(HttpStatus.OK.value());

        if(!fetchedPosts.isEmpty()){
            builder
                    .statusMessage("Posts fetched correctly!")
                    .totalObjects(fetchedPosts.size())
                    .returnedObjects(fetchedPosts.size())
                    .payload(fetchedPosts);

        }else{
            builder
                    .statusMessage("No post found with the given tag(s)")
                    .totalObjects(0)
                    .returnedObjects(0);
        }

        APIResponse<List<TagDTO>> response = builder.build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
