package org.example;
import org.example.entities.cat.Cat;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersDao;
import org.example.valueObjects.Color;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class OwnersControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private OwnersDao ownersDao;
    private MockMvc mockMvc;
    private Owner testOwner;
    private Cat testCat;
    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }
    @BeforeEach
    void setUp() {
        testOwner = new Owner(LocalDate.parse("2004-12-12"), new ArrayList<>());
        testCat = new Cat("Tina", "British shorthair", Color.GREY,
                testOwner, LocalDate.parse("2017-08-04"), new ArrayList<>());
        testOwner.getCats().add(testCat);
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
        this.mockMvc.perform(post("/owner").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isOk());
    }
    @Test
    public void savingOwnerTest_ShouldReturn400Status() throws Exception {
        var request = "{\"birthday\" : null}";
        var json = new ObjectMapper().writeValueAsString(request);
        this.mockMvc.perform(post("/owner")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void getOwnerTest_ShouldReturn200Status() throws Exception {
        ownersDao.save(testOwner);
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/owner/{ownerId}", testOwner.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }
    @Test
    public void getOwnerTest_ShouldReturn400Status() throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/owner/5000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError()).andReturn();
    }
    @Test
    public void deleteOwnerTest_ShouldReturn200Status() throws Exception {
        ownersDao.save(testOwner);
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.delete("/owner/{ownerId}", testOwner.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }
}
