import org.example.Color;
import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.example.entities.cat.CatsDaoImpl;
import org.example.entities.cat.FindCriteria;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersDao;
import org.example.entities.owner.OwnersDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CatsDaoTests {
    Cat testCat;
    Owner testOwner;
    CatsDao catsDao;
    OwnersDao ownersDao;
    @BeforeEach
    void setUp() {
        testOwner = new Owner(LocalDate.parse("2004-12-12"), new ArrayList<>());
        testCat = new Cat("Tina", "British shorthair", Color.GREY,
                testOwner, LocalDate.parse("2017-08-04"), new ArrayList<>());
        testOwner.getCats().add(testCat);
        catsDao = new CatsDaoImpl();
        ownersDao = new OwnersDaoImpl();
    }
    @Test
    void saveCat() {
        ownersDao.save(testOwner);
        var savedCat = catsDao.save(testCat);
        assert(savedCat.getId() != null);
        Cat foundCat = catsDao.findById(savedCat.getId());
        assertEquals(savedCat.getId(), foundCat.getId());
        assertEquals(savedCat.getBirthday(), foundCat.getBirthday());
        assertEquals(savedCat.getBreed(), foundCat.getBreed());
        assertIterableEquals(savedCat.getFriends(), foundCat.getFriends());
        assertEquals(savedCat.getColor(), foundCat.getColor());
        assertEquals(savedCat.getOwner().getId(), foundCat.getOwner().getId());
        catsDao.deleteById(savedCat.getId());
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
        var criteria = new FindCriteria();
        criteria.setName("Tina");
        List<Cat> foundCats = catsDao.findByCriteria(criteria);
        var foundIds = foundCats.stream().map(Cat::getId).toList();

        assert(!foundIds.contains(thirdSavedCat.getId()));
        assert(foundIds.contains(savedCat.getId()));
        assert(foundIds.contains(secondSavedCat.getId()));
        catsDao.deleteById(savedCat.getId());
        catsDao.deleteById(secondSavedCat.getId());
        catsDao.deleteById(thirdSavedCat.getId());
    }
}
