package it.salvatoreabello.simpleblogapp.serviceImpl;

import it.salvatoreabello.simpleblogapp.model.TagModel;
import it.salvatoreabello.simpleblogapp.repository.ITagRepository;
import it.salvatoreabello.simpleblogapp.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements ITagService {
    @Autowired
    private ITagRepository repository;

    @Override
    public List<TagModel> findByTagnameIn(List<String> tags){
        return repository.findByTagnameIn(tags);
    }
}
