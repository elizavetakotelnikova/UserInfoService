package org.example;
import org.example.valueObjects.Color;
import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.example.entities.owner.FindCriteria;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OwnersDaoTests {
    Owner testOwner;
    Cat testCat;
    @Autowired
    CatsDao catsDao;
    @Autowired
    OwnersDao ownersDao;
    @BeforeEach
    void setUp() {
        testOwner = new Owner(LocalDate.parse("2004-12-12"), new ArrayList<>());
        testCat = new Cat("Tina", "British shorthair", Color.GREY,
                testOwner, LocalDate.parse("2017-08-04"), new ArrayList<>());
        testOwner.getCats().add(testCat);
    }
    @Test
    void saveOwner() {
        var savedOwner = ownersDao.save(testOwner);
        var savedCat = catsDao.save(testCat);
        assert(savedOwner.getId() != null);
        Owner foundOwner = ownersDao.findById(savedOwner.getId()).get();
        var foundIds = foundOwner.getCats().stream().map(Cat::getId).toList();
        assertEquals(foundOwner.getBirthday(), savedOwner.getBirthday());
        assert(foundOwner.getCats().size() == 1);
        assert(foundIds.contains(savedCat.getId()));
    }
    @Test
    void findOwnerByCriteria() {
        var savedOwner = ownersDao.save(testOwner);
        var savedCat = catsDao.save(testCat);
        var secondOwner = new Owner(LocalDate.parse("2003-12-23"), new ArrayList<>());
        ownersDao.save(secondOwner);
        assert(savedOwner.getId() != null);
        /*var criteria = new FindCriteria(null, null);
        criteria.setBirthday(LocalDate.parse("2003-12-23"));
        List<Owner> foundOwners = ownersDao.findByCriteria(criteria);*/
        List<Owner> foundOwners = ownersDao.findByBirthday(LocalDate.parse("2003-12-23"));
        var foundIds = foundOwners.stream().map(Owner::getId).toList();
        assert(!foundIds.contains(savedOwner.getId()));
        assert(foundIds.contains(secondOwner.getId()));
        ownersDao.deleteById(secondOwner.getId());
        catsDao.deleteById(savedCat.getId());
    }
    @Test
    void findOwnerById() {
        var savedOwner = ownersDao.save(testOwner);
        var savedCat = catsDao.save(testCat);
        assert(savedOwner.getId() != null);
        Owner foundOwner = ownersDao.findById(savedOwner.getId()).get();
        var foundIds = foundOwner.getCats().stream().map(Cat::getId).toList();
        assertEquals(foundOwner.getBirthday(), savedOwner.getBirthday());
        assert(foundOwner.getCats().size() == 1);
        assert(foundIds.contains(savedCat.getId()));
    }
    @Test
    void deleteOwner() {
        var savedOwner = ownersDao.save(testOwner);
        var savedCat = catsDao.save(testCat);
        ownersDao.deleteById(savedOwner.getId());
        var foundOwner = ownersDao.findById(savedOwner.getId());
        assert(foundOwner.isEmpty());
        //assert(foundOwner == null);
    }
}
