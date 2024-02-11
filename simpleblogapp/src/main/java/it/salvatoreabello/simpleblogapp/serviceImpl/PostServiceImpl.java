package it.salvatoreabello.simpleblogapp.serviceImpl;

import it.salvatoreabello.simpleblogapp.model.PostModel;
import it.salvatoreabello.simpleblogapp.model.TagModel;
import it.salvatoreabello.simpleblogapp.repository.IPostRepository;
import it.salvatoreabello.simpleblogapp.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements IPostService {
    @Autowired
    private IPostRepository repository;

    @Override
    public List<PostModel> getAll() {
        return repository.findAll(Sort.by(Sort.Order.desc("title")));
    }

    @Override
    public PostModel saveOrUpdate(PostModel entity){
        return repository.save(entity);
    }
}
