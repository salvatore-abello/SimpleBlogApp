package it.salvatoreabello.simpleblogapp.serviceImpl;

import it.salvatoreabello.simpleblogapp.config.JWTUtil;
import it.salvatoreabello.simpleblogapp.dto.UserDTO;
import it.salvatoreabello.simpleblogapp.model.UserModel;
import it.salvatoreabello.simpleblogapp.repository.IUserRepository;
import it.salvatoreabello.simpleblogapp.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JWTUtil jwt;

    @Override
    public List<UserDTO> getAll() {
        return repository.findAll(Sort.by(Sort.Order.desc("id")))
                .stream()
                .map(m -> {
                    m.setPosts(null);
                    return modelMapper.map(m, UserDTO.class);
                })
                .collect(Collectors.toList());
    }
    @Override
    public UserModel findByEmail(String email){
        if(email == null) return null;
        return repository.findByEmail(email);
    }
    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public UserDTO findById(Integer id) {
        if (id == null) return null;
        Optional<UserModel> userOptional = repository.findById(id);
        return userOptional.map(m -> modelMapper.map(m, UserDTO.class)).orElse(null);
    }

    @Override
    public void setEncodedPassword(UserModel entity) throws MethodArgumentNotValidException{
        String password = entity.getPassword();

        if(password.length() < 6){
            BindingResult bindingResult = new BeanPropertyBindingResult(entity, "password");
            bindingResult.rejectValue("password", "length", "Password must be at least 6 characters long");

            try { // Exception is usually thrown when a field is invalid.
                throw new MethodArgumentNotValidException(
                        new MethodParameter(
                                this.getClass().getMethod("setEncodedPassword", UserModel.class),
                                0),
                        bindingResult);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        entity.setPassword(passwordEncoder.encode(password));
    }

    @Override
    public UserDTO saveOrUpdate(UserModel entity) throws MethodArgumentNotValidException {
        this.setEncodedPassword(entity);
        return modelMapper.map(repository.save(entity), UserDTO.class);
    }

    @Override
    public void changePassword(String currentPassword,
                                  String newPassword) throws Exception {

        UserModel currentUser = repository.findById(
                jwt.getCurrentUser()
                        .getId())
                .orElseThrow(() -> new Exception("Unexpected error"));


        if(!passwordEncoder.matches(currentPassword, currentUser.getPassword()))
            throw new Exception("Wrong password");

        currentUser.setPassword(newPassword);
        this.setEncodedPassword(currentUser);

        repository.save(currentUser);
    }

    public Boolean login(UserModel entityFromReq, UserModel entityFromDb) {
        String password = entityFromReq.getPassword();
        return password != null && passwordEncoder.matches(password, entityFromDb.getPassword());
    }
}
