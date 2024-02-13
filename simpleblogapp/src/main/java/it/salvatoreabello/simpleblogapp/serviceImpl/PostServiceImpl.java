package it.salvatoreabello.simpleblogapp.serviceImpl;

import it.salvatoreabello.simpleblogapp.dto.PostDTO;
import it.salvatoreabello.simpleblogapp.dto.TagDTO;
import it.salvatoreabello.simpleblogapp.dto.UserDTO;
import it.salvatoreabello.simpleblogapp.model.PostModel;
import it.salvatoreabello.simpleblogapp.model.TagModel;
import it.salvatoreabello.simpleblogapp.model.UserModel;
import it.salvatoreabello.simpleblogapp.repository.IPostRepository;
import it.salvatoreabello.simpleblogapp.repository.ITagRepository;
import it.salvatoreabello.simpleblogapp.service.IPostService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.description.modifier.Ownership;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements IPostService {
    @Autowired
    private IPostRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ITagRepository tagRepository;

    @PersistenceContext
    EntityManager entityManager;

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
    public List<PostModel> findByTagnameIn(List<String> tags){
        return repository.findByTagsTagnameIn(tags);
    }

    @Override
    public PostDTO saveOrUpdate(PostDTO post) {
        // I don't like this method
        // THANKS I LOVE YOU SO MUCH https://www.youtube.com/watch?v=kzsGuDcAGkE

        PostModel newPost = PostModel.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .owner(UserModel.builder().id(1).build())
                .tags(new ArrayList<>())
                .build();


        newPost.getTags().addAll(
                post.getTags().stream().map(
                        t -> {
                            Optional<TagModel> tagOptional = tagRepository.findById(t.getId());
                            TagModel tt = tagOptional.get(); // Add some kind of validation here
                            tt.getPosts().add(newPost);
                            return tt;
                        }
                ).toList()
        );



        PostModel savedPost = repository.save(newPost);

        return modelMapper.map(savedPost, PostDTO.class);
    }

    @Override
    public List<PostDTO> searchPosts(List<String> tags, String content,
                                     String title,
                                     Integer ownerId){
        return repository.findByTagsTagnameInAndOwnerIdAndContentAndTitle(content, title, tags, ownerId)
                .stream()
                .map(m -> modelMapper.map(m, PostDTO.class))
                .toList();
    }
}
