package it.salvatoreabello.simpleblogapp.service;

import it.salvatoreabello.simpleblogapp.dto.PostDTO;
import it.salvatoreabello.simpleblogapp.model.PostModel;

import java.util.List;


public interface IPostService {
    List<PostDTO> getAll();
    PostModel saveOrUpdate(PostModel entity);
}
