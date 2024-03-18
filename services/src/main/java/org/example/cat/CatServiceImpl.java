package org.example.cat;

import lombok.AllArgsConstructor;
import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.example.entities.cat.FindCriteria;
import org.example.entities.owner.OwnersDao;
import org.example.exceptions.IncorrectArgumentsException;
import java.util.List;

@AllArgsConstructor
public class CatServiceImpl implements CatService {
    private CatsDao catsDao;
    private OwnersDao ownersDao;
    @Override
    public Cat saveCat(catSavingDto dto) throws IncorrectArgumentsException {
        if (dto.getName() != null || dto.getBirthday() != null || dto.getBreed() != null || dto.getOwnerId() != null) {
            var owner = ownersDao.findById(dto.getOwnerId());
            var friends = dto.getFriendsId().stream().map(catsDao::findById).toList();
            return catsDao.save(new Cat(dto.getName(), dto.getBreed(), dto.getColor(), owner, dto.getBirthday(), friends));
        }
        throw new IncorrectArgumentsException("Incorrect data provided, unable to save a cat");
    }

    @Override
    public List<Cat> getCatByCriteria(FindCriteria criteria) {
        return catsDao.findByCriteria(criteria);
    }

    @Override
    public Cat getCatById(long id) {
        return catsDao.findById(id);
    }

    @Override
    public void friendCats(long firstId, long secondId) {
        if (checkIfCatsAreFriends(firstId, secondId)) return;
        var firstCat = catsDao.findById(firstId);
        var secondCat = catsDao.findById(secondId);
        firstCat.getFriends().add(secondCat);
        catsDao.update(firstCat);
        catsDao.update(secondCat);
    }
    @Override
    public void delete(long id) {
        catsDao.deleteById(id);
    }
    public boolean checkIfCatsAreFriends(long firstId, long secondId) {
        var firstCat = catsDao.findById(firstId);
        var friends = firstCat.getFriends().stream().map(Cat::getId).toList();
        return friends.contains(secondId);
    }
}
