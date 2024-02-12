package it.salvatoreabello.simpleblogapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private int id;
    private String title;
    private String content;

    @JsonIgnoreProperties({"posts"})
    private UserDTO owner;

    @JsonIgnoreProperties({"posts"})
    private List<TagDTO> tags;
}