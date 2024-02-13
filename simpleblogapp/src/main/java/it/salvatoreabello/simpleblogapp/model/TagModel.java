package it.salvatoreabello.simpleblogapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Data;
import java.util.List;

@Getter
@Setter
@Entity
@Data
@Table(name = "tags")
public class TagModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String tagname;

    @ManyToMany(mappedBy = "tags")
    private List<PostModel> posts;
}
