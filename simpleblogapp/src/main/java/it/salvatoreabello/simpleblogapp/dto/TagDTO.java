package it.salvatoreabello.simpleblogapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PostDTO> posts;
}