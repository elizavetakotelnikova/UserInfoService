package org.example.owner;

import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersDao;
import org.example.entities.owner.FindCriteria;
import org.example.entities.user.RolesDao;
import org.example.exceptions.IncorrectArgumentsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {
    private final CatsDao catsDao;
    private final OwnersDao ownersDao;
    private final RolesDao rolesDao;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public OwnerServiceImpl(CatsDao catsDao, OwnersDao ownersDao, RolesDao rolesDao, PasswordEncoder passwordEncoder) {
        this.catsDao = catsDao;
        this.ownersDao = ownersDao;
        this.rolesDao = rolesDao;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public Owner saveOwner(OwnerInfoDto dto) throws IncorrectArgumentsException {
        if (dto.getBirthday() == null) throw new IncorrectArgumentsException("Incorrect data provided, unable to create an owner");
        List<Cat> cats = new ArrayList<>();
        if (dto.getCats() != null) cats = dto.getCats().stream().map(x -> catsDao.findById(x.getId()).get()).toList();
        return ownersDao.save(new Owner(dto.getBirthday(), cats));
    }
     @Override
    public List<Owner> getOwnerByCriteria(FindCriteria criteria) {
        if (criteria.getBirthday() != null) return ownersDao.findByBirthday(criteria.getBirthday());
        return ownersDao.findAll();
    }

    @Override
    public Owner getOwnerById(long id) {
        return ownersDao.findById(id);
    }
    @Override
    public Owner update(OwnerInfoDto dto) throws IncorrectArgumentsException {
        if (dto.getBirthday() == null || dto.getId() == null) throw new IncorrectArgumentsException("Incorrect data provided, unable to update an owner");
        var cats = dto.getCats().stream().map(x -> catsDao.findById(x.getId()).get()).toList();
        return ownersDao.save(new Owner(dto.getBirthday(), cats));
    }

    @Override
    public void delete(long id) {
        ownersDao.deleteById(id);
    }
}
