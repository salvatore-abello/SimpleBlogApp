package it.salvatoreabello.simpleblogapp.repository;

import it.salvatoreabello.simpleblogapp.model.PostModel;
import it.salvatoreabello.simpleblogapp.model.TagModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITagRepository extends JpaRepository<TagModel, Integer> {
    List<TagModel> findByTagnameIn(@Param("tagnames") List<String> tagnames);
}