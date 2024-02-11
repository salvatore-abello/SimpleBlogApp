package it.salvatoreabello.simpleblogapp.service;

import it.salvatoreabello.simpleblogapp.model.TagModel;


import java.util.List;

public interface ITagService {
    List<TagModel> findByTagnameIn(List<String> tagnames);
}
