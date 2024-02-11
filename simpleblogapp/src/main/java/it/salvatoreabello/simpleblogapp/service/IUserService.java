package it.salvatoreabello.simpleblogapp.service;

import it.salvatoreabello.simpleblogapp.model.UserModel;

import java.util.List;


public interface IUserService {
    List<UserModel> getAll();
    UserModel findById(Integer id);
    UserModel findByEmail(String email);
    UserModel saveOrUpdate(UserModel entity);
    Boolean login(String email, String password);
}
