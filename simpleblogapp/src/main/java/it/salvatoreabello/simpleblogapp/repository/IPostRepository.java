package it.salvatoreabello.simpleblogapp.repository;

import it.salvatoreabello.simpleblogapp.model.PostModel;
import it.salvatoreabello.simpleblogapp.model.TagModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IPostRepository extends JpaRepository<PostModel, Integer> {
    // https://stackoverflow.com/questions/33438483/spring-data-jpa-query-manytomany
    List<PostModel> findByTags_TagnameIn(@Param("tagnames") List<String> tagnames);
}