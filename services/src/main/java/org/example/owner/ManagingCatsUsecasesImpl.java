package org.example.owner;

import lombok.AllArgsConstructor;
import org.example.entities.cat.CatsDao;
import org.example.entities.owner.OwnersDao;

@AllArgsConstructor
public class ManagingCatsUsecasesImpl implements ManagingCatsUsecases{
    OwnersDao ownersDao;
    CatsDao catsDao;

    @Override
    public void addToCatList(long ownerId, long catId) {
        var owner = ownersDao.findById(ownerId);
        var cat = catsDao.findById(catId);
        owner.getCats().add(cat);
        ownersDao.update(owner);
    }

    @Override
    public void removeFromCatList(long ownerId, long catId) {
        var owner = ownersDao.findById(ownerId);
        var cat = catsDao.findById(catId);
        owner.getCats().remove(cat);
        ownersDao.update(owner);
    }
}
