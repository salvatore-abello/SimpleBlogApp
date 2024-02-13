package it.salvatoreabello.simpleblogapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "posts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message="Title cannot be empty")
    @Pattern(regexp="[\\s\\S]{3,32}")
    private String title;

    @NotEmpty(message="Title cannot be empty")
    @Pattern(regexp="[\\s\\S]{1,65536}")
    private String content;

    @ManyToOne
    @JoinColumn(name = "CODOwner", referencedColumnName = "id")
    private UserModel owner;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name= "Tags_Posts",
            joinColumns = @JoinColumn(name = "CODPost", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "CODTag", nullable = false)
    )
    private List<TagModel> tags;
}
