package com.example.ownermicroservice.services;

import com.example.jpa.Cat;
import com.example.jpa.Owner;
import com.example.ownermicroservice.repositories.OwnersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagingCatsUsecasesImpl implements ManagingCatsUsecases {
    OwnersDao ownersDao;
    @Autowired
    public ManagingCatsUsecasesImpl(OwnersDao ownersDao) {
        this.ownersDao = ownersDao;
    }

    @Override
    public void addToCatList(Owner owner, Cat cat) {
        owner.getCats().add(cat);
        ownersDao.save(owner);
    }

    @Override
    public void removeFromCatList(Owner owner, Cat cat) {
        owner.getCats().remove(cat);
        ownersDao.save(owner);
    }
}
