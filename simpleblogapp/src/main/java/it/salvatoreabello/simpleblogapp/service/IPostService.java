package it.salvatoreabello.simpleblogapp.service;

import it.salvatoreabello.simpleblogapp.dto.PostDTO;
import it.salvatoreabello.simpleblogapp.model.PostModel;

import java.util.List;


public interface IPostService {
    PostDTO findById(Integer id);
    List<PostDTO> getAll();
    List<PostDTO> searchPosts(List<String> tags, String content, String title, Integer ownerId);
    List<PostModel> findByTagnameIn(List<String> tagnames);
    PostDTO saveOrUpdate(PostDTO post);
}
