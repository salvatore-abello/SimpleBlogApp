package it.salvatoreabello.simpleblogapp.service;

import it.salvatoreabello.simpleblogapp.dto.TagDTO;

import java.util.List;

public interface ITagService {
    List<TagDTO> findByTagnameIn(List<String> tagnames);
}
