package org.example.cat;
import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.example.entities.cat.FindCriteria;
import org.example.entities.owner.OwnersDao;
import org.example.exceptions.IncorrectArgumentsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatServiceImpl implements CatService {
    private final CatsDao catsDao;
    private final OwnersDao ownersDao;
    @Autowired
    public CatServiceImpl(CatsDao catsDao, OwnersDao ownersDao) {
        this.catsDao = catsDao;
        this.ownersDao = ownersDao;
    }
    @Override
    public Cat saveCat(CatInfoDto dto) throws IncorrectArgumentsException {
        if (dto.getName() != null && dto.getBirthday() != null && dto.getBreed() != null && dto.getOwnerId() != null) {
            var owner = ownersDao.findById(dto.getOwnerId()).stream().findFirst().orElse(null);
            var friends = dto.getFriendsId().stream().map(x -> catsDao.findById(x).stream().findFirst().orElse(null)).toList();
            return catsDao.save(new Cat(dto.getName(), dto.getBreed(), dto.getColor(), owner, dto.getBirthday(), friends));
        }
        throw new IncorrectArgumentsException("Incorrect data provided, unable to save a cat");
    }

    /*@Override
    public List<Cat> getCatByCriteria(FindCriteria criteria) {

        return catsDao.findByCriteria(criteria);
    }*/
    @Override
    public List<Cat> getCatByCriteria(FindCriteria criteria) {
        if (criteria.getColor() != null) return catsDao.findByColor(criteria.getColor());
        if (criteria.getBirthday() != null) return catsDao.findByBirthday(criteria.getBirthday());
        if (criteria.getName() != null) return catsDao.findByName(criteria.getName());
        if (criteria.getBreed() != null) return catsDao.findByBreed(criteria.getBreed());
        if (criteria.getOwnerId() != null) return catsDao.findByOwnerId(criteria.getOwnerId());
        return catsDao.findAll();
    }

    @Override
    public Cat getCatById(long id) {
        return catsDao.findById(id);
    }
    @Override
    public void delete(long id) {
        catsDao.deleteById(id);
    }
    @Override
    public Cat update(CatInfoDto dto) throws IncorrectArgumentsException {
        if (dto.getId() != null && dto.getName() != null && dto.getBirthday() != null && dto.getBreed() != null && dto.getOwnerId() != null) {
            var owner = ownersDao.findById(dto.getOwnerId()).stream().findFirst().orElse(null);
            var friends = dto.getFriendsId().stream().map(x -> catsDao.findById(x).stream().findFirst().orElse(null)).toList();
            return catsDao.save(new Cat(dto.getId(), dto.getName(), dto.getBreed(), dto.getColor(), owner, dto.getBirthday(), friends));
        }
        throw new IncorrectArgumentsException("Incorrect data provided, unable to update a cat");
    }
}
