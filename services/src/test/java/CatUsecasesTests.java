import org.example.valueObjects.Color;
import org.example.cat.CatServiceImpl;
import org.example.cat.FriendUsecasesImpl;
import org.example.cat.catSavingDto;
import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersDao;
import org.example.exceptions.IncorrectArgumentsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CatUsecasesTests {
    Cat testCat;
    Owner testOwner;
    @Mock
    CatsDao catsDao;
    @Mock
    OwnersDao ownersDao;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        testOwner = new Owner(LocalDate.parse("2004-12-12"), new ArrayList<>());
        testCat = new Cat("Tina", "British shorthair", Color.GREY,
                testOwner, LocalDate.parse("2017-08-04"), new ArrayList<>());
        testOwner.getCats().add(testCat);
        testCat.setId(1L);
        testOwner.setId(1L);
        testCat.setOwner(testOwner);
    }
    @Test
    void saveCatUsecase() {
        var catService = new CatServiceImpl(catsDao, ownersDao);
        /*try {
            var cat = catService.saveCat(new catSavingDto(testCat.getName(), testCat.getBreed(),
                    testCat.getColor(), testCat.getId(), testCat.getBirthday(), new ArrayList<>()));
            verify(catsDao, times(1)).save(cat);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        Exception exception = assertThrows(IncorrectArgumentsException.class, () -> catService.saveCat(new catSavingDto(null, testCat.getBreed(),
                testCat.getColor(), testCat.getId(), testCat.getBirthday(), new ArrayList<>())));
        assertEquals(IncorrectArgumentsException.class, exception.getClass());
    }
    @Test
    void deleteCatUsecase() {
        var catService = new CatServiceImpl(catsDao, ownersDao);
        try {
            when(catsDao.findById(1)).thenReturn(testCat);
            catService.delete(1);
            verify(catsDao, times(1)).deleteById(1);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        /*Exception exception = assertThrows(IncorrectArgumentsException.class, () -> catService.saveCat(new catSavingDto(null, testCat.getBreed(),
                testCat.getColor(), testCat.getId(), testCat.getBirthday(), new ArrayList<>())));
        assertEquals(IncorrectArgumentsException.class, exception.getClass());*/
    }

    @Test
    void friendCatsUsecase() {
        var catService = new CatServiceImpl(catsDao, ownersDao);
        var secondCat = new Cat("Mark", "Britain shorthair", Color.GREY,
                testOwner, LocalDate.parse("2022-02-20"), new ArrayList<>());
        var friendService = new FriendUsecasesImpl(catsDao);
        when(catsDao.findById(1)).thenReturn(testCat);
        when(catsDao.findById(2)).thenReturn(secondCat);
        friendService.friendCats(1, 2);
        assert(testCat.getFriends().contains(secondCat));
        assert(secondCat.getFriends().contains(testCat));
    }
}
