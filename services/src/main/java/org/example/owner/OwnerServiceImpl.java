package org.example.owner;

import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersDao;
import org.example.entities.owner.FindCriteria;
import org.example.entities.owner.RolesDao;
import org.example.valueObjects.Role;
import org.example.exceptions.IncorrectArgumentsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private boolean validateUser(OwnerInfoDto dto) {
        if (dto.getBirthday() == null) return false;
        if (dto.getUsername() == null) return false;
        if (dto.getPassword() == null) return false;
        return true;
    }
    @Override
    public Owner saveOwner(OwnerInfoDto dto) throws IncorrectArgumentsException {
        if (!validateUser(dto)) throw new IncorrectArgumentsException("Incorrect data provided, unable to create an owner");
        if (ownersDao.findOwnerByUsername(dto.getUsername()) != null) throw new IncorrectArgumentsException("User with such username already exists");
        List<Cat> cats = new ArrayList<>();
        if (dto.getCats() != null) cats = dto.getCats().stream().map(x -> catsDao.findById(x.getId()).get()).toList();
        if (dto.getAuthorities() == null) {
            var roles = new ArrayList<org.example.entities.owner.Role>();
            roles.add(rolesDao.findRoleByAuthority("ROLE_USER"));
            dto.setAuthorities(roles);
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return ownersDao.save(new Owner(dto.getBirthday(), cats, dto.getUsername(), dto.getPassword().getBytes(), dto.getAuthorities()));
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
        if (!validateUser(dto) || dto.getId() == null) throw new IncorrectArgumentsException("Incorrect data provided, unable to update an owner");
        var cats = dto.getCats().stream().map(x -> catsDao.findById(x.getId()).get()).toList();
        if (dto.getAuthorities() == null) {
            var roles = new ArrayList<org.example.entities.owner.Role>();
            roles.add(new org.example.entities.owner.Role(Role.ROLE_USER.toString()));
            dto.setAuthorities(roles);
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return ownersDao.save(new Owner(dto.getBirthday(), cats, dto.getUsername(), dto.getPassword().getBytes(), dto.getAuthorities()));
    }

    @Override
    public Owner getOwnerByUsername(String username) {
        return ownersDao.findOwnerByUsername(username);
    }

    @Override
    public String getToken(String username, String password) {
        return null;
    }

    @Override
    public void delete(long id) {
        ownersDao.deleteById(id);
    }
}
