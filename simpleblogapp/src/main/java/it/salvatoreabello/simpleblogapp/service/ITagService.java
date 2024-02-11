package it.salvatoreabello.simpleblogapp.service;


import it.salvatoreabello.simpleblogapp.model.PostModel;
import it.salvatoreabello.simpleblogapp.model.TagModel;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITagService {
    List<TagModel> findByTagnameIn(List<String> tagnames);
}
