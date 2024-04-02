import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersDao;
import org.example.valueObjects.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CatsDaoTests {
    Cat testCat;
    Owner testOwner;
    @Autowired
    CatsDao catsDao;
    @Autowired
    OwnersDao ownersDao;
    @BeforeEach
    void setUpEntities() {
        testOwner = new Owner(LocalDate.parse("2004-12-12"), new ArrayList<>());
        testCat = new Cat("Tina", "British shorthair", Color.GREY,
                testOwner, LocalDate.parse("2017-08-04"), new ArrayList<>());
        testOwner.getCats().add(testCat);
    }
    @Test
    void saveCatTest() {
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
    }
    @Test
    void saveCatWithFriendsTest() {
        ownersDao.save(testOwner);
        var secondTestCat = new Cat("Tina", "British shorthair", Color.GREY,
                testOwner, LocalDate.parse("2017-09-04"), new ArrayList<>());
        var savedCat = catsDao.save(testCat);
        secondTestCat.getFriends().add(testCat);
        var secondSavedCat = catsDao.save(secondTestCat);
        testCat.getFriends().add(secondTestCat);
        catsDao.update(savedCat);
        assert(savedCat.getId() != null);
        Cat foundCat = catsDao.findById(savedCat.getId()).get();
        Cat foundSecondCat = catsDao.findById(secondSavedCat.getId()).get();
        assert(foundSecondCat.getFriends().getFirst().getId().equals(testCat.getId()));
        assert(foundCat.getFriends().getFirst().getId().equals(secondSavedCat.getId()));
    }
    @Test
    void deleteCatTest() {
        ownersDao.save(testOwner);
        var savedCat = catsDao.save(testCat);
        catsDao.deleteById(savedCat.getId());
        var foundCat = catsDao.findById(savedCat.getId());
        assert(foundCat == null);
    }
}