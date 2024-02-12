package it.salvatoreabello.simpleblogapp.serviceImpl;

import it.salvatoreabello.simpleblogapp.dto.PostDTO;
import it.salvatoreabello.simpleblogapp.model.PostModel;
import it.salvatoreabello.simpleblogapp.repository.IPostRepository;
import it.salvatoreabello.simpleblogapp.service.IPostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements IPostService {
    @Autowired
    private IPostRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PostDTO> getAll() {
        return repository.findAll(Sort.by(Sort.Order.desc("title")))
                .stream()
                .map(m -> modelMapper.map(m, PostDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostModel saveOrUpdate(PostModel entity) {
        return repository.save(entity);
    }
}
