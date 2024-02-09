package it.salvatoreabello.simpleblogapp.serviceImpl;

import it.salvatoreabello.simpleblogapp.model.UserModel;
import it.salvatoreabello.simpleblogapp.repository.IUserRepository;
import it.salvatoreabello.simpleblogapp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepository repository;


    @Override
    public UserModel saveOrUpdate(UserModel entity){
        return repository.save(entity);
    }
}
