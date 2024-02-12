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
public class TagDTO {
    private Integer id;
    private String tagname;

    @JsonIgnoreProperties({"tags"})
    private List<PostDTO> posts;
}