import org.example.entities.user.RolesDao;
import org.example.entities.user.UsersDao;
import org.example.valueObjects.Color;
import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.example.owner.ManagingCatsUsecasesImpl;
import org.example.owner.OwnerServiceImpl;
import org.example.owner.OwnerInfoDto;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersDao;
import org.example.exceptions.IncorrectArgumentsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    UsersDao usersDao;
    @Mock
    CatsDao catsDao;
    @Mock
    RolesDao rolesDao;
    @Autowired
    PasswordEncoder passwordEncoder;
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
    void saveOwnerUsecaseTest() {
        var ownerService = new OwnerServiceImpl(catsDao, ownersDao, usersDao, rolesDao, passwordEncoder);
        Exception exception = assertThrows(IncorrectArgumentsException.class, () -> ownerService.saveOwner(new OwnerInfoDto(null, null, new ArrayList<>())));
        assertEquals(IncorrectArgumentsException.class, exception.getClass());
    }
    @Test
    void deleteOwnerUsecaseTest() {
        var ownerService = new OwnerServiceImpl(catsDao, ownersDao, usersDao, rolesDao, passwordEncoder);
        try {
            when(ownersDao.findById(1)).thenReturn(testOwner);
            ownerService.delete(1);
            verify(ownersDao, times(1)).deleteById(1);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        /*Exception exception = assertThrows(IncorrectArgumentsException.class, () -> ownerService.saveOwner(new ownerSavingDto(null, testOwner.getBreed(),
                testOwner.getColor(), testOwner.getId(), testOwner.getBirthday(), new ArrayList<>())));
        assertEquals(IncorrectArgumentsException.class, exception.getClass());*/
    }

    @Test
    void addCatUsecaseTest() {
        var ownerService = new OwnerServiceImpl(catsDao, ownersDao, usersDao, rolesDao, passwordEncoder);
        var catManagingService = new ManagingCatsUsecasesImpl(ownersDao, catsDao);
        when(ownersDao.findById(1)).thenReturn(testOwner);
        catManagingService.addToCatList(testOwner.getId(), testCat.getId());
        assert(testOwner.getCats().contains(testCat));
        verify(ownersDao, times(1)).save(testOwner);
    }
}