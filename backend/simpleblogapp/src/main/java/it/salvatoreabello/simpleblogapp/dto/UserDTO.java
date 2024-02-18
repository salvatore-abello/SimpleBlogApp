package it.salvatoreabello.simpleblogapp.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO {
    private int id;
    private String name;
    private String surname;

    @JsonIgnoreProperties({"owner"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PostDTO> posts;
}
