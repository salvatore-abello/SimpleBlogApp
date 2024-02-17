package it.salvatoreabello.simpleblogapp.repository;

import it.salvatoreabello.simpleblogapp.model.PostModel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IPostRepository extends JpaRepository<PostModel, Integer> {
    // https://stackoverflow.com/questions/33438483/spring-data-jpa-query-manytomany
    List<PostModel> findByTagsTagnameIn(@Param("tagnames") List<String> tagnames);

    @Override // This is still too slow!
    @Query(value="SELECT DISTINCT a FROM PostModel a JOIN FETCH a.tags")
    List<PostModel> findAll();

    // I don't know if it's possible doing this using only method names
    @Query("select p from PostModel p inner join p.tags tags " +
            "where (:tagnames is null or tags.tagname in :tagnames) " +
            "and (:ownerid is null or p.owner.id = :ownerid) " +
            "and (:content is null or p.content like %:content%) " +
            "and (:title is null or p.title like %:title%)")
    List<PostModel> findByTagsTagnameInAndOwnerIdAndContentAndTitle(
            @Param("content") String content,
            @Param("title") String title,
            @Param("tagnames") List<String> tagnames,
            @Param("ownerid") Integer id);
    List<PostModel> findByOwnerId(@Param("id") Integer id);
}