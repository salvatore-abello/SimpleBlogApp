package it.salvatoreabello.simpleblogapp.repository;

import it.salvatoreabello.simpleblogapp.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, Integer> {
}
