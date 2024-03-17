package repositoryTests;
import org.example.Color;
import org.example.entities.cat.Cat;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersDao;
import org.example.entities.owner.OwnersDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CatsRepositoryTests {
    Cat testCat;
    Owner testOwner;
    CatsRepository catsRepository;
    OwnersDao ownersRepository;
    @BeforeEach
    void setUp() {
        testOwner = new Owner(null, LocalDate.parse("2004-12-12"), new ArrayList<>());
        testCat = new Cat(null, "Tina", "British shorthair", Color.GREY, LocalDate.parse("2017-08-04"), new ArrayList<>());
        catsRepository = new CatsRepositoryImpl();
        ownersRepository = new OwnersDaoImpl();
    }
    @Test
    void saveCat() {
        ownersRepository.create(testOwner);
        catsRepository.create(testCat);
        assert(testCat.getId() != null);
        Cat cat = catsRepository.getById(testCat.getId());
        assertEquals(testCat, cat);
    }

}
