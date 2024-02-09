package it.salvatoreabello.simpleblogapp.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tags")
public class TagModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public List<PostModel> getPosts() {
        return posts;
    }

    public void setPosts(List<PostModel> posts) {
        this.posts = posts;
    }

    private String tagname;

    @ManyToMany(mappedBy = "tags")
    private List<PostModel> posts;
}
