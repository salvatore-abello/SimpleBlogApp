package it.salvatoreabello.simpleblogapp.service;

import it.salvatoreabello.simpleblogapp.dto.TagDTO;
import it.salvatoreabello.simpleblogapp.model.TagModel;

import java.util.List;

public interface ITagService {
    List<TagDTO> getAll();
}
