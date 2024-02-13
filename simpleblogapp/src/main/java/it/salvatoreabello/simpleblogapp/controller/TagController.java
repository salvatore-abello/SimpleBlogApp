package it.salvatoreabello.simpleblogapp.controller;

import it.salvatoreabello.simpleblogapp.config.APIResponse;
import it.salvatoreabello.simpleblogapp.dto.PostDTO;
import it.salvatoreabello.simpleblogapp.dto.TagDTO;
import it.salvatoreabello.simpleblogapp.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private ITagService tagService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<List<TagDTO>>> getAllTags(){
        APIResponse.APIResponseBuilder<List<TagDTO>> builder = APIResponse.builder();
        List<TagDTO> tags = tagService.getAll();
        builder.statusCode(HttpStatus.OK.value());

        if(!tags.isEmpty()){
            builder
                    .statusCode(HttpStatus.OK.value())
                    .statusMessage("Tags fetched correctly")
                    .totalObjects(tags.size())
                    .returnedObjects(tags.size())
                    .payload(tags).build();
        }else{
            builder
                    .statusCode(HttpStatus.OK.value())
                    .statusMessage("No tags found")
                    .totalObjects(0)
                    .returnedObjects(0)
                    .build();
        }

        APIResponse<List<TagDTO>> response = builder.build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
