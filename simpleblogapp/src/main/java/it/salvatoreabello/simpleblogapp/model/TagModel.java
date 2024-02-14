package it.salvatoreabello.simpleblogapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tags")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String tagname;

    @ManyToMany(mappedBy = "tags")
    private List<PostModel> posts;
}
