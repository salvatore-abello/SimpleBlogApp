package it.salvatoreabello.simpleblogapp.service;

import it.salvatoreabello.simpleblogapp.model.UserModel;

import java.util.List;


public interface IUserService {
    UserModel saveOrUpdate(UserModel entity);
}
