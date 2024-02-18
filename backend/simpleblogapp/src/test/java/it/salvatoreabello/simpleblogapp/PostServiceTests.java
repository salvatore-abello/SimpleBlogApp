package it.salvatoreabello.simpleblogapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.salvatoreabello.simpleblogapp.config.APIResponse;
import it.salvatoreabello.simpleblogapp.dto.PostDTO;
import it.salvatoreabello.simpleblogapp.dto.TagDTO;
import it.salvatoreabello.simpleblogapp.dto.UserDTO;
import it.salvatoreabello.simpleblogapp.service.IPostService;
import it.salvatoreabello.simpleblogapp.service.ITagService;
import it.salvatoreabello.simpleblogapp.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PostServiceTests {

    /*

    PostDTO findById(Integer id);
    List<PostModel> findByTagnameIn(List<String> tagnames);

     */

    @Autowired
    private IPostService postService;

    @Autowired
    private MockMvc mvc;

    private String jwtToken;

    @BeforeEach
    public void setupPostsAndTestsaveOrUpdate() throws Exception {
        int numberOfPosts = randint(2, 10);

        for(int npost = 0; npost < numberOfPosts; npost++){
            // String json = new ObjectMapper().writeValueAsString(yourObjectHere);

            Map<String, Object> json = new HashMap<>();
            json.put("email", "admin@test.com");
            json.put("password", "adminpassword");

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(json);

            byte[] jsonBytes = jsonString.getBytes();

            // login
            MvcResult result = mvc.perform(post("/api/auth/login").with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBytes)
                            .characterEncoding("utf-8"))
                    .andExpect(status().isOk())
                    .andReturn();

            HashMap<String, Object> map = objectMapper
                    .readValue(result.getResponse().getContentAsString(), new TypeReference<HashMap<String,Object>>(){});

            json.clear();
            String jwtToken = (String) map.get("payload");

            assertNotNull(jwtToken, "JWT Token cannot be null");

            this.jwtToken = jwtToken;
            log.debug(this.jwtToken);

            // post creation
            json.clear();

            List<TagDTO> tags = new ArrayList<>();

            log.info("Testing normal tags");
            for(int i = 0; i < randint(0, 3); i++){
                 tags.add(TagDTO.builder()
                         .id(randint(1, 4))
                         .build());
            }

            assertNotNull(tags, "Error while adding tags");

            jsonString = objectMapper.writeValueAsString(PostDTO.builder()
                    .title("searchme" + randstr(16))
                    .content("searchme2" + randstr(1024))
                    .tags(tags)
                    .build());

            jsonBytes = jsonString.getBytes();
            result = mvc.perform(post("/api/posts/create").with(csrf())
                            .header("Authorization", "Bearer " + this.jwtToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBytes)
                            .characterEncoding("utf-8"))
                    .andExpect(status().isOk())
                    .andReturn();

            json.clear();
        }

    }

    @Test
    public void testInvalidNumberOfTags() throws Exception {
        log.info("Testing invalid number of tags. This should not work");
        List<TagDTO> tags = new ArrayList<>();
        for(int i = 0; i < randint(10, 30); i++){
            tags.add(TagDTO.builder()
                    .id(randint(1,4))
                    .build());
        }

        assertNotNull(tags, "Error while adding tags");

        String jsonString = new ObjectMapper().writeValueAsString(PostDTO.builder()
                .title("searchme" + randstr(16))
                .content("searchme2" + randstr(1024))
                .tags(tags)
                .build());

        byte[] jsonBytes = jsonString.getBytes();
        MvcResult result = mvc.perform(post("/api/posts/create").with(csrf())
                        .header("Authorization", "Bearer " + this.jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBytes)
                        .characterEncoding("utf-8"))
                .andExpect(status().is5xxServerError())
                .andReturn();
    }

    @Test
    public void testOwnerOverwrite() throws Exception {
        log.info("Trying to overwrite the owner. This should not work");
        List<TagDTO> tags = new ArrayList<>();
        for(int i = 0; i < randint(0, 4); i++){
            tags.add(TagDTO.builder()
                    .id(randint(1, 4))
                    .build());
        }

        int arbitraryOwnerId = randint(2, 5); // admin=1, we don't need it

        String jsonString = new ObjectMapper().writeValueAsString(PostDTO.builder()
                .title("searchme" + randstr(16))
                .content("searchme2" + randstr(1024))
                .tags(tags)
                .owner(UserDTO.builder()
                        .id(arbitraryOwnerId)
                        .name("NewUser")
                        .surname("NewUser")
                        .build())
                .build());

        byte[] jsonBytes = jsonString.getBytes();
        MvcResult result = mvc.perform(post("/api/posts/create").with(csrf())
                        .header("Authorization", "Bearer " + this.jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBytes)
                        .characterEncoding("utf-8"))
                .andReturn();

        assertTrue(postService.searchPosts(null, null, null, arbitraryOwnerId).isEmpty(),
                "It's possible to create posts with arbitrary owner");

    }

    @Test
    public void testSearchPosts(){
        assertFalse(postService.searchPosts(null, "searchme2", "searchme", 1).isEmpty(),
                "searchPosts is not working properly");
    }

    @Test
    public void testFindById(){
        assertNotNull(postService.findById(1), "This should not be null");
    }

    @Test
    public void testFindByTagnameIn(){
        List<String> tags = new ArrayList<>();
        tags.add("Tag1");
        tags.add("Tag2");

        assertFalse(postService.findByTagnameIn(tags).isEmpty());
    }

    @Test
    public void testGetAll(){
        assertFalse(postService.getAll().isEmpty(), "Something went wrong");
    }

    public static int randint(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    private String randstr(int length) {
        String ALPHA_CHARS = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALPHA_CHARS.length());
            char randomChar = ALPHA_CHARS.charAt(randomIndex);
            builder.append(randomChar);
        }

        return builder.toString();
    }
}
