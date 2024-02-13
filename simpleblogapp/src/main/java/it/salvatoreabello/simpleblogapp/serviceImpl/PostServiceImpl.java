package it.salvatoreabello.simpleblogapp.serviceImpl;

import it.salvatoreabello.simpleblogapp.dto.PostDTO;
import it.salvatoreabello.simpleblogapp.dto.TagDTO;
import it.salvatoreabello.simpleblogapp.dto.UserDTO;
import it.salvatoreabello.simpleblogapp.model.PostModel;
import it.salvatoreabello.simpleblogapp.model.UserModel;
import it.salvatoreabello.simpleblogapp.repository.IPostRepository;
import it.salvatoreabello.simpleblogapp.repository.ITagRepository;
import it.salvatoreabello.simpleblogapp.service.IPostService;
import it.salvatoreabello.simpleblogapp.service.ITagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements IPostService {
    @Autowired
    private IPostRepository repository;

    @Autowired
    private ITagRepository tagRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PostDTO findById(Integer id) {
        if (id == null) return null;
        Optional<PostModel> postOptional = repository.findById(id);
        return postOptional.map(m -> modelMapper.map(m, PostDTO.class)).orElse(null);
    }

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

    @Override
    public List<PostModel> findByTagnameIn(List<String> tags){
        return repository.findByTags_TagnameIn(tags);
    }

    @Override
    public List<PostDTO> searchPosts(List<String> tags, String content, String title){
        // if tags == null, fetch all posts, else, filter all posts by the given tags
        List<PostDTO> fetchedPosts =  (tags == null
                ? repository.findAll(Sort.by(Sort.Order.desc("title")))
                : repository.findByTags_TagnameIn(tags))
                .stream()
                .map(m -> modelMapper.map(m, PostDTO.class))
                .toList();

        if (content != null && !content.isEmpty()) {
            fetchedPosts = fetchedPosts.stream()
                    .filter(p -> p.getContent().contains(content))
                    .toList();
        }

        if (title != null && !title.isEmpty()) {
            fetchedPosts = fetchedPosts.stream()
                    .filter(p -> p.getTitle().contains(title))
                    .toList();
        }


        return fetchedPosts;
    }
}
