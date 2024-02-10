package it.salvatoreabello.simpleblogapp.serviceImpl;

import it.salvatoreabello.simpleblogapp.model.UserModel;
import it.salvatoreabello.simpleblogapp.repository.IUserRepository;
import it.salvatoreabello.simpleblogapp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepository repository;
    @Override
    public List<UserModel> getAll(){
        return repository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    @Override
    public UserModel findById(Integer id) {
        System.out.println(id);
        if (id == null) {
            return null;
        }
        Optional<UserModel> userOptional = repository.findById(id);
        return userOptional.orElse(null);
    }

    @Override
    public UserModel saveOrUpdate(UserModel entity){
        return repository.save(entity);
    }

}
