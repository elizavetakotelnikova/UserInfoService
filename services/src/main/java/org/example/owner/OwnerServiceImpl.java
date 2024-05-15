package org.example.owner;

import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersDao;
import org.example.entities.owner.FindCriteria;
import org.example.entities.user.RolesDao;
import org.example.entities.user.UsersDao;
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
    private final UsersDao usersDao;
    private final RolesDao rolesDao;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public OwnerServiceImpl(CatsDao catsDao, OwnersDao ownersDao, UsersDao usersDao, RolesDao rolesDao, PasswordEncoder passwordEncoder) {
        this.catsDao = catsDao;
        this.ownersDao = ownersDao;
        this.usersDao = usersDao;
        this.rolesDao = rolesDao;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public Owner saveOwner(OwnerInfoDto dto) throws IncorrectArgumentsException {
        if (dto.getBirthday() == null || dto.getUserId() == null) throw new IncorrectArgumentsException("Incorrect data provided, unable to create an owner");
        if (ownersDao.findByUser(usersDao.findById(dto.getUserId()).get()) != null) throw new IncorrectArgumentsException("Incorrect data provided, owner with same userId already exists");
        List<Cat> cats = new ArrayList<>();
        if (dto.getCats() != null) cats = dto.getCats().stream().map(x -> catsDao.findById(x.getId()).get()).toList();
        var user = usersDao.findById(dto.getUserId()).get();
        return ownersDao.save(new Owner(dto.getBirthday(), cats, user));
    }
     @Override
    public List<Owner> getOwnerByCriteria(FindCriteria criteria) {
        if (criteria.getBirthday() != null) return ownersDao.findByBirthday(criteria.getBirthday());
        if (criteria.getUserId() != null) {
            List<Owner> ownersList = new ArrayList<>();
            var owner = ownersDao.findByUser(usersDao.findById(criteria.getUserId()).orElse(null));
            ownersList.add(owner);
            return ownersList;
        }
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
        var user = usersDao.findById(dto.getUserId()).get();
        return ownersDao.save(new Owner(dto.getId(), dto.getBirthday(), cats, user));
    }

    @Override
    public void delete(long id) {
        ownersDao.deleteById(id);
    }
}
