package org.example.owner;

import lombok.AllArgsConstructor;
import org.example.entities.cat.CatsDao;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ManagingCatsUsecasesImpl implements ManagingCatsUsecases{
    OwnersDao ownersDao;
    CatsDao catsDao;
    @Autowired
    public ManagingCatsUsecasesImpl(CatsDao catsDao, OwnersDao ownersDao) {
        this.ownersDao = ownersDao;
        this.catsDao = catsDao;
    }

    @Override
    public void addToCatList(long ownerId, long catId) {
        var owner = ownersDao.findById(ownerId);
        var cat = catsDao.findById(catId);
        owner.getCats().add(cat);
        ownersDao.save(owner);
    }

    @Override
    public void removeFromCatList(long ownerId, long catId) {
        var owner = ownersDao.findById(ownerId);
        var cat = catsDao.findById(catId);
        owner.getCats().remove(cat);
        ownersDao.save(owner);
    }
}
