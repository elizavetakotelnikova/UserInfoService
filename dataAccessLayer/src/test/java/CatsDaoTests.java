import org.example.Color;
import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.example.entities.cat.CatsDaoImpl;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersDao;
import org.example.entities.owner.OwnersDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Equals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

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
        var savedOwner = ownersDao.create(testOwner);
        var savedCat = catsDao.save(testCat);
        //testCat.setOwnerId(testOwner.getId());
        assert(savedCat.getId() != null);
        Cat foundCat = catsDao.findById(savedCat.getId());
        assertEquals(savedCat.getId(), foundCat.getId());
        assertEquals(savedCat.getBirthday(), foundCat.getBirthday());
        assertEquals(savedCat.getBreed(), foundCat.getBreed());
        assertIterableEquals(savedCat.getFriends(), foundCat.getFriends());
        assertEquals(savedCat.getColor(), foundCat.getColor());
        assertEquals(savedCat.getOwner().getId(), foundCat.getOwner().getId());
    }
}
