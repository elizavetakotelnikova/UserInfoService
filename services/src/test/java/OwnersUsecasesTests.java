import org.example.Color;
import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.example.owner.ManagingCatsUsecasesImpl;
import org.example.owner.OwnerServiceImpl;
import org.example.owner.ownerSavingDto;
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

class OwnersUsecasesTests {
    Owner testOwner;
    Cat testCat;
    @Mock
    OwnersDao ownersDao;
    @Mock
    CatsDao catsDao;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        testOwner = new Owner(LocalDate.parse("2004-12-12"), new ArrayList<>());
        testCat = new Cat("Tina", "British shorthair", Color.GREY,
                testOwner, LocalDate.parse("2017-08-04"), new ArrayList<>());
        testOwner.getCats().add(testCat);
        testOwner.setId(1L);
        testCat.setId(1L);
        testCat.setOwner(testOwner);
    }
    @Test
    void saveOwnerUsecase() {
        var ownerService = new OwnerServiceImpl(catsDao, ownersDao);
        /*try {
            var owner = ownerService.saveOwner(new ownerSavingDto(testOwner.getName(), testOwner.getBreed(),
                    testOwner.getColor(), testOwner.getId(), testOwner.getBirthday(), new ArrayList<>()));
            verify(ownersDao, times(1)).save(owner);
        }
        ownerch (Exception e) {
            throw new RuntimeException(e);
        }*/
        Exception exception = assertThrows(IncorrectArgumentsException.class, () -> ownerService.saveOwner(new ownerSavingDto(null, new ArrayList<>())));
        assertEquals(IncorrectArgumentsException.class, exception.getClass());
    }
    @Test
    void deleteOwnerUsecase() {
        var ownerService = new OwnerServiceImpl(catsDao, ownersDao);
        try {
            when(ownersDao.findById(1)).thenReturn(testOwner);
            ownerService.delete(1);
            verify(ownersDao, times(1)).delete(1);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        /*Exception exception = assertThrows(IncorrectArgumentsException.class, () -> ownerService.saveOwner(new ownerSavingDto(null, testOwner.getBreed(),
                testOwner.getColor(), testOwner.getId(), testOwner.getBirthday(), new ArrayList<>())));
        assertEquals(IncorrectArgumentsException.class, exception.getClass());*/
    }

    @Test
    void addCatUsecase() {
        var ownerService = new OwnerServiceImpl(catsDao, ownersDao);
        var catManagingService = new ManagingCatsUsecasesImpl(ownersDao, catsDao);
        when(ownersDao.findById(1)).thenReturn(testOwner);
        catManagingService.addToCatList(testOwner.getId(), testCat.getId());
        assert(testOwner.getCats().contains(testCat));
        verify(ownersDao, times(1)).update(testOwner);
    }
}