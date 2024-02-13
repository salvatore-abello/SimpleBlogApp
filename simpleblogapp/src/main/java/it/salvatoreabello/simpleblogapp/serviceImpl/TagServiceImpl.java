package it.salvatoreabello.simpleblogapp.serviceImpl;

import it.salvatoreabello.simpleblogapp.dto.TagDTO;
import it.salvatoreabello.simpleblogapp.model.TagModel;
import it.salvatoreabello.simpleblogapp.repository.ITagRepository;
import it.salvatoreabello.simpleblogapp.service.ITagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements ITagService {
    @Autowired
    private ITagRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<TagDTO> findByTagnameIn(List<String> tags){
        return repository.findByTagnameIn(tags)
                .stream()
                .map(m -> modelMapper.map(m, TagDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TagDTO> getAll(){ // is this ok?
        return repository.findAll(Sort.by(Sort.Order.desc("id")))
                .stream()
                .map(m -> TagDTO.builder()
                        .id(m.getId())
                        .tagname(m.getTagname())
                        .build())
                .collect(Collectors.toList());
    }
}
