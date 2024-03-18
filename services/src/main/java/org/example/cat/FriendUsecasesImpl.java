package org.example.cat;

import lombok.AllArgsConstructor;
import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;

@AllArgsConstructor
public class FriendUsecasesImpl implements FriendUsecases{
    CatsDao catsDao;
    @Override
    public void friendCats(long firstId, long secondId) {
        if (checkIfCatsAreFriends(firstId, secondId)) return;
        var firstCat = catsDao.findById(firstId);
        var secondCat = catsDao.findById(secondId);
        firstCat.getFriends().add(secondCat);
        secondCat.getFriends().add(firstCat);
        catsDao.update(firstCat);
        catsDao.update(secondCat);
    }

    @Override
    public void unfriendCats(long firstId, long secondId) {
        if (!checkIfCatsAreFriends(firstId, secondId)) return;
        var firstCat = catsDao.findById(firstId);
        var secondCat = catsDao.findById(secondId);
        firstCat.getFriends().remove(secondCat);
        secondCat.getFriends().remove(firstCat);
        catsDao.update(firstCat);
        catsDao.update(secondCat);
    }

    @Override
    public boolean checkIfCatsAreFriends(long firstId, long secondId) {
        var firstCat = catsDao.findById(firstId);
        var friends = firstCat.getFriends().stream().map(Cat::getId).toList();
        return friends.contains(secondId);
    }
}
