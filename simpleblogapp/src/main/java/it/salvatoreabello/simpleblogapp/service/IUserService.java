package it.salvatoreabello.simpleblogapp.service;

import it.salvatoreabello.simpleblogapp.model.UserModel;

import java.util.List;


public interface IUserService {
    List<UserModel> getAll();
    UserModel findById(Integer id);
    UserModel saveOrUpdate(UserModel entity);
}
