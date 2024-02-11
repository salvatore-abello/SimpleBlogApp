package it.salvatoreabello.simpleblogapp.serviceImpl;

import it.salvatoreabello.simpleblogapp.model.UserModel;
import it.salvatoreabello.simpleblogapp.repository.IUserRepository;
import it.salvatoreabello.simpleblogapp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserModel> getAll(){
        return repository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    @Override
    public UserModel findByEmail(String email){
        if(email == null) return null;
        return repository.findByEmail(email);
    }
    @Override
    public UserModel findById(Integer id) {
        if (id == null) return null;
        Optional<UserModel> userOptional = repository.findById(id);
        return userOptional.orElse(null);
    }

    @Override
    public UserModel saveOrUpdate(UserModel entity){
        return repository.save(entity);
    }

    public Boolean login(String email, String password) {
        UserModel user = repository.findByEmail(email);
        return password != null && passwordEncoder.matches(password, user.getPassword());
    }
}
