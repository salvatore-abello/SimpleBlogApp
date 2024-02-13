package it.salvatoreabello.simpleblogapp.service;

import it.salvatoreabello.simpleblogapp.dto.PostDTO;
import it.salvatoreabello.simpleblogapp.dto.TagDTO;
import it.salvatoreabello.simpleblogapp.dto.UserDTO;
import it.salvatoreabello.simpleblogapp.model.PostModel;

import java.util.List;


public interface IPostService {
    PostDTO findById(Integer id);
    List<PostDTO> getAll();
    List<PostDTO> searchPosts(List<String> tags, String content, String title);
    List<PostModel> findByTagnameIn(List<String> tagnames);
    PostModel saveOrUpdate(PostModel entity);
}
