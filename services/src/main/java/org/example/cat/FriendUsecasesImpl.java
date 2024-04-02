package org.example.cat;
import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendUsecasesImpl implements FriendUsecases{
    CatsDao catsDao;
    @Autowired
    public FriendUsecasesImpl(CatsDao catsDao) {
        this.catsDao = catsDao;
    }
    @Override
    public void friendCats(long firstId, long secondId) {
        if (checkIfCatsAreFriends(firstId, secondId)) return;
        var firstCat = catsDao.findById(firstId);
        var secondCat = catsDao.findById(secondId);
        firstCat.getFriends().add(secondCat);
        secondCat.getFriends().add(firstCat);
        catsDao.save(firstCat);
        catsDao.save(secondCat);
    }

    @Override
    public void unfriendCats(long firstId, long secondId) {
        if (!checkIfCatsAreFriends(firstId, secondId)) return;
        var firstCat = catsDao.findById(firstId);
        var secondCat = catsDao.findById(secondId);
        firstCat.getFriends().remove(secondCat);
        secondCat.getFriends().remove(firstCat);
        catsDao.save(firstCat);
        catsDao.save(secondCat);
    }

    @Override
    public boolean checkIfCatsAreFriends(long firstId, long secondId) {
        var firstCat = catsDao.findById(firstId);
        var friends = firstCat.getFriends().stream().map(Cat::getId).toList();
        return friends.contains(secondId);
    }
}
