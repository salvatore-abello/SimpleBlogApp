package it.salvatoreabello.simpleblogapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.salvatoreabello.simpleblogapp.config.JWTUtil;
import it.salvatoreabello.simpleblogapp.dto.UserDTO;
import it.salvatoreabello.simpleblogapp.model.UserModel;
import it.salvatoreabello.simpleblogapp.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql(scripts = {"classpath:test-schema.sql", "classpath:test-data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserServiceTests {

    private final String testPrefix = randstr(8);
    private final int ITERATIONS = 8;

    @Autowired
    private IUserService userService;

    @Autowired
    private JWTUtil jwt;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void setupAndTestSaveOrUpdate() throws MethodArgumentNotValidException {
        String email, password, name, surname;

        // Used to create posts
        userService.saveOrUpdate(UserModel.builder()
                .name("admin")
                .surname("admin")
                .email("admin@test.com")
                .password("adminpassword")
                .build());

        for(int i = 0; i < ITERATIONS; i++){
            email = testPrefix + i + "@test.com";
            password = testPrefix + i + "password";
            name = testPrefix;
            surname = testPrefix;
            try {
                UserDTO registeredUser =  userService.saveOrUpdate(UserModel.builder()
                        .name(name)
                        .surname(surname)
                        .email(email)
                        .password(password)
                        .build());

                assertEquals(registeredUser.getName(), name);
                assertEquals(registeredUser.getSurname(), surname);
            } catch (MethodArgumentNotValidException e) {
                fail("Something went wrong during user registration");
            }
        }
    }

    @Test
    public void testFindByEmailAndLogin(){

        String email, password;

        userService.getAll().forEach(m -> {
            log.info(m.toString());
        });

        log.info(String.valueOf(userService.getAll().size()));

        for(int i = 0; i < ITERATIONS; i++){
            email = testPrefix + i + "@test.com";
            password = testPrefix + i + "password";

            UserModel fromDb = userService.findByEmail(email);
            assertNotNull(fromDb, "Unable to retrieve user by email");

            UserModel fromReq = UserModel.builder()
                    .email(email)
                    .password(password)
                    .build();

            assertTrue(userService.login(fromReq, fromDb), "Unable to login");
        }
    }

    @Test
    public void testFindById(){
        UserDTO user;
        for(int i = 0; i < ITERATIONS; i++){
            user = userService.findById(i + 2); // Because we added admin before
            assertEquals(user.getName(), testPrefix);
            assertEquals(user.getSurname(), testPrefix);
        }
    }

    @Test
    public void testJWTAndChangepassword() throws Exception {
        String email, currentPassword, newPassword;
        for(int i = 0; i < ITERATIONS; i++){
            email = testPrefix + i + "@test.com";
            currentPassword = testPrefix + i + "password";
            newPassword = testPrefix + i + "newpassword";

            Map<String, Object> json = new HashMap<>();
            json.put("email", email);
            json.put("password", currentPassword);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(json);

            byte[] jsonBytes = jsonString.getBytes();

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

            json.put("newPassword", newPassword);
            json.put("currentPassword", currentPassword);

            jsonBytes = objectMapper.writeValueAsString(json).getBytes();

            result = mvc.perform(post("/api/auth/changepassword").with(csrf())
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBytes)
                            .characterEncoding("utf-8"))
                    .andExpect(status().isOk())
                    .andReturn();

            log.info(result.getResponse().getContentAsString());
            log.info("Login test after password changed");

            UserModel fromDb = userService.findByEmail(email);
            assertNotNull(fromDb, "Unable to retrieve user by email");

            UserModel fromReq = UserModel.builder()
                    .email(email)
                    .password(newPassword)
                    .build();

            assertTrue(userService.login(fromReq, fromDb), "Unable to login");
        }
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
