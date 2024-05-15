package org.example;
import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersDao;
import org.example.entities.user.User;
import org.example.entities.user.UsersDao;
import org.example.security.CustomUser;
import org.example.valueObjects.Color;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class OwnersControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private OwnersDao ownersDao;
    @Autowired
    private CatsDao catsDao;
    @Autowired
    private UsersDao usersDao;
    private MockMvc mockMvc;
    private User testUser;
    private Owner testOwner;
    private Cat testCat;
    private UserDetails userDetails;
    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).apply(springSecurity()).build();
    }
    @BeforeEach
    void setUp() {
        testCat = new Cat("Tina", "British shorthair", Color.GREY,
                testOwner, LocalDate.parse("2017-08-04"), new ArrayList<>());
        testUser = new User("username", "password", new ArrayList<>());
        usersDao.save(testUser);
        testOwner = new Owner(LocalDate.parse("2004-12-12"), new ArrayList<>(), testUser);
        ownersDao.save(testOwner);
        testOwner.getCats().add(testCat);
        userDetails = new CustomUser(testUser.getId(), testUser.getUsername(), testUser.getPassword(),
                new ArrayList<>());
    }
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    @Test
    public void savingOwnerTest_ShouldReturn200Status() throws Exception {
        var request = "{\"birthday\" : \"2004-01-30\"}";
        this.mockMvc.perform(post("/owner").accept(MediaType.APPLICATION_JSON).with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isOk());
    }
    @Test
    public void savingOwnerTest_ShouldReturn400Status() throws Exception {
        var request = "{\"birthday\" : null}";
        var json = new ObjectMapper().writeValueAsString(request);
        this.mockMvc.perform(post("/owner").with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void getOwnerTest_ShouldReturn200Status() throws Exception {
        ownersDao.save(testOwner);
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/owner/{ownerId}", testOwner.getId()).with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }
    @Test
    public void getOwnerTest_ShouldReturn400Status() throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/owner/5000").with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError()).andReturn();
    }
    @Test
    public void deleteOwnerTest_ShouldReturn200Status() throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.delete("/owner/{ownerId}", testOwner.getId()).with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }
}
