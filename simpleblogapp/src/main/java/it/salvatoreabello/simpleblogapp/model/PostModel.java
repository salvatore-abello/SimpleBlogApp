package it.salvatoreabello.simpleblogapp.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
            name="TagsPosts",
            joinColumns = @JoinColumn(name = "CODPost"),
            inverseJoinColumns = @JoinColumn(name = "CODTag")
    )
    private List<TagModel> tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
    }

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }
}
