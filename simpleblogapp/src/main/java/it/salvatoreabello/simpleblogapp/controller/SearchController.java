package it.salvatoreabello.simpleblogapp.controller;

import it.salvatoreabello.simpleblogapp.config.APIError;
import it.salvatoreabello.simpleblogapp.config.APIResponse;
import it.salvatoreabello.simpleblogapp.config.ErrorDetail;
import it.salvatoreabello.simpleblogapp.model.TagModel;
import it.salvatoreabello.simpleblogapp.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    @Autowired
    private ITagService tagService;

    @GetMapping(value = {"/posts"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<List<TagModel>>> searchPostsByTags(@RequestParam("tags") List<String> tags){
        APIResponse.APIResponseBuilder<List<TagModel>> builder = APIResponse.builder();
        try{
            List<TagModel> fetchedPosts = tagService.findByTagnameIn(tags);
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

            APIResponse<List<TagModel>> response = builder.build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    APIResponse.<List<TagModel>>builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .statusMessage("Internal server error while fetching posts")
                            .totalObjects(0)
                            .returnedObjects(0)
                            .errors(
                                    APIError.builder()
                                            .timestamp(new Date())
                                            .path("/api/search/posts")
                                            .details(
                                                    Collections.singletonList(
                                                            ErrorDetail.builder()
                                                                    .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                                                                    .field("generic")
                                                                    .source("repository.findByTagnameIn")
                                                                    .message(e.getMessage()).build()
                                                    )
                                            ).build()
                            )
                            .build()

            );
        }
    }
}
