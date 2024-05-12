package org.example;

import org.example.valueObjects.Color;
import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersDao;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
@SpringBootTest(classes = TestDataAccessLayerApplication.class)
class CatsDaoTestsWithTestContainers {
    private Cat testCat;
    private Owner testOwner;
    @Autowired
    private CatsDao catsDao;
    @Autowired
    private OwnersDao ownersDao;
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

    @BeforeEach
    void setUp() throws SQLException {
        Connection connectionProvider = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        var flyaway = setupFlyway(postgres);
        flyaway.clean();
        flyaway.migrate();
    }
    private Flyway setupFlyway(PostgreSQLContainer container) {
        return new Flyway(
                Flyway.configure()
                        .locations("/db.migration")
                        .dataSource(container.getJdbcUrl(), container.getUsername(),
                                container.getPassword())
        );
    }
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    @BeforeEach
    void setUpEntities() {
        testOwner = new Owner(LocalDate.parse("2004-12-12"), new ArrayList<>());
        testCat = new Cat("Tina", "British shorthair", Color.GREY,
                testOwner, LocalDate.parse("2017-08-04"), new ArrayList<>());
        testOwner.getCats().add(testCat);
    }
    @Test
    void saveCat() {
        ownersDao.save(testOwner);
        var savedCat = catsDao.save(testCat);
        assert(savedCat.getId() != null);
        Cat foundCat = catsDao.findById(savedCat.getId()).get();
        assertEquals(savedCat.getId(), foundCat.getId());
        assertEquals(savedCat.getBirthday(), foundCat.getBirthday());
        assertEquals(savedCat.getBreed(), foundCat.getBreed());
        assertIterableEquals(savedCat.getFriends(), foundCat.getFriends());
        assertEquals(savedCat.getColor(), foundCat.getColor());
        assertEquals(savedCat.getOwner().getId(), foundCat.getOwner().getId());
        catsDao.deleteById(savedCat.getId());
    }
    @Test
    void saveCatWithFriends() {
        ownersDao.save(testOwner);
        var secondTestCat = new Cat("Tina", "British shorthair", Color.GREY,
                testOwner, LocalDate.parse("2017-09-04"), new ArrayList<>());
        var savedCat = catsDao.save(testCat);
        secondTestCat.getFriends().add(testCat);
        var secondSavedCat = catsDao.save(secondTestCat);
        testCat.getFriends().add(secondTestCat);
        catsDao.save(savedCat);
        assert(savedCat.getId() != null);
        Cat foundCat = catsDao.findById(savedCat.getId()).get();
        Cat foundSecondCat = catsDao.findById(secondSavedCat.getId()).get();
        assert(foundSecondCat.getFriends().getFirst().getId().equals(testCat.getId()));
        assert(foundCat.getFriends().getFirst().getId().equals(secondSavedCat.getId()));
        catsDao.deleteById(savedCat.getId());
        catsDao.deleteById(secondSavedCat.getId());
    }
    @Test
    void findCatByCriteria() {
        ownersDao.save(testOwner);
        var savedCat = catsDao.save(testCat);
        var secondTestCat = new Cat("Tina", "British shorthair", Color.GREY,
                testOwner, LocalDate.parse("2017-09-04"), new ArrayList<>());
        var secondSavedCat = catsDao.save(secondTestCat);
        var thirdTestCat = new Cat("Marsik", "British shorthair", Color.GREY,
                testOwner, LocalDate.parse("2017-08-04"), new ArrayList<>());
        var thirdSavedCat = catsDao.save(thirdTestCat);
        assert(savedCat.getId() != null);
        /*var criteria = new FindCriteria();
        criteria.setName("Tina");*/
        List<Cat> foundCats = catsDao.findByName("Tina");
        var foundIds = foundCats.stream().map(Cat::getId).toList();

        assert(!foundIds.contains(thirdSavedCat.getId()));
        assert(foundIds.contains(savedCat.getId()));
        assert(foundIds.contains(secondSavedCat.getId()));
        catsDao.deleteById(savedCat.getId());
        catsDao.deleteById(secondSavedCat.getId());
        catsDao.deleteById(thirdSavedCat.getId());
    }
}
