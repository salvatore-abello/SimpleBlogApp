package it.salvatoreabello.simpleblogapp.service;

import it.salvatoreabello.simpleblogapp.dto.UserDTO;
import it.salvatoreabello.simpleblogapp.model.UserModel;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;


public interface IUserService {
    List<UserDTO> getAll();
    UserDTO findById(Integer id);
    UserModel findByEmail(String email); // only used during login
    void changePassword(String currentPassword, String newPassword) throws Exception;
    UserDTO saveOrUpdate(UserModel entity) throws MethodArgumentNotValidException;
    Boolean login(UserModel entityFromReq, UserModel entityFromDb);
    void setEncodedPassword(UserModel entity) throws MethodArgumentNotValidException;
}
