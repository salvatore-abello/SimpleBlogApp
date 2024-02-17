package it.salvatoreabello.simpleblogapp.service;

import it.salvatoreabello.simpleblogapp.dto.PostDTO;
import it.salvatoreabello.simpleblogapp.model.PostModel;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface IPostService {
    // @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    PostDTO findById(Integer id);
    // @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    List<PostDTO> getAll();
    // @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)

    List<PostDTO> searchPosts(List<String> tags, String content, String title, Integer ownerId);
    List<PostModel> findByTagnameIn(List<String> tagnames);
    PostDTO saveOrUpdate(PostDTO post) throws Exception;
}
