package it.salvatoreabello.simpleblogapp.service;

import it.salvatoreabello.simpleblogapp.model.PostModel;
import it.salvatoreabello.simpleblogapp.model.UserModel;

import java.util.List;


public interface IPostService {
    List<PostModel> getAll();
    PostModel saveOrUpdate(PostModel entity);
}
