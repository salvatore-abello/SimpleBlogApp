package it.salvatoreabello.simpleblogapp.repository;

import it.salvatoreabello.simpleblogapp.model.PostModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IPostRepository extends JpaRepository<PostModel, Integer> {
}