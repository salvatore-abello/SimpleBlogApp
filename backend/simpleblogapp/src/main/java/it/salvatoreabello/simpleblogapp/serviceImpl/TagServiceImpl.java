package it.salvatoreabello.simpleblogapp.serviceImpl;

import it.salvatoreabello.simpleblogapp.dto.TagDTO;
import it.salvatoreabello.simpleblogapp.repository.ITagRepository;
import it.salvatoreabello.simpleblogapp.service.ITagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
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

    @Autowired
    private CacheManager cacheManager;

    @Override
    @Cacheable("tags")
    public List<TagDTO> getAll(){ // is this ok?
        System.out.println("tags.GETALL CALLED");
        return repository.findAll(Sort.by(Sort.Order.desc("id")))
                .stream()
                .map(m -> TagDTO.builder()
                        .id(m.getId())
                        .tagname(m.getTagname())
                        .build())
                .collect(Collectors.toList());
    }
}
