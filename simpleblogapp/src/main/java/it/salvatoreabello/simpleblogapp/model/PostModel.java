package it.salvatoreabello.simpleblogapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "posts")
public class PostModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String content;
    @ManyToOne
    @JoinColumn(name = "CODOwner", referencedColumnName = "id")
    private UserModel owner;
    @ManyToMany
    @JoinTable(
            name="Tags_Posts",
            joinColumns = @JoinColumn(name = "CODPost"),
            inverseJoinColumns = @JoinColumn(name = "CODTag")
    )
    private List<TagModel> tags;
}
