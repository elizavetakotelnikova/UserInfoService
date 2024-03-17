package repositoryTests;
import org.example.Color;
import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsRepository;
import org.example.entities.cat.CatsRepositoryImpl;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersRepository;
import org.example.entities.owner.OwnersRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CatsRepositoryTests {
    Cat testCat;
    Owner testOwner;
    CatsRepository catsRepository;
    OwnersRepository ownersRepository;
    @BeforeEach
    void setUp() {
        testOwner = new Owner(null, LocalDate.parse("2004-12-12"), new ArrayList<>());
        testCat = new Cat(null, "Tina", "British shorthair", Color.GREY,
                testOwner.getId(), LocalDate.parse("2017-08-04"), new ArrayList<>());
        catsRepository = new CatsRepositoryImpl();
        ownersRepository = new OwnersRepositoryImpl();
    }
    @Test
    void saveCat() {
        ownersRepository.create(testOwner);
        testCat.setOwnerId(testOwner.getId());
        catsRepository.create(testCat);
        assert(testCat.getId() != null);
        Cat cat = catsRepository.getById(testCat.getId());
        assertEquals(testCat, cat);
    }

}
