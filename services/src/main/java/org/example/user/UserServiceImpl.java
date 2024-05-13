package org.example.user;
import org.example.entities.cat.CatsDao;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersDao;
import org.example.entities.user.FindCriteria;
import org.example.entities.user.RolesDao;
import org.example.entities.user.User;
import org.example.entities.user.UsersDao;
import org.example.exceptions.IncorrectArgumentsException;
import org.example.valueObjects.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final CatsDao catsDao;
    private final OwnersDao ownersDao;
    private final UsersDao usersDao;
    private final RolesDao rolesDao;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(OwnersDao ownersDao, CatsDao catsDao, UsersDao usersDao, RolesDao rolesDao, PasswordEncoder passwordEncoder) {
        this.ownersDao = ownersDao;
        this.catsDao = catsDao;
        this.usersDao = usersDao;
        this.rolesDao = rolesDao;
        this.passwordEncoder = passwordEncoder;
    }
    private boolean validateUser(UserInfoDto dto) {
        if (dto.getOwnerId() != null) {
            if (ownersDao.findById(dto.getOwnerId()).isEmpty()) {
                return false;
            }
        }
        if (dto.getUsername() == null) return false;
        if (dto.getPassword() == null) return false;
        return true;
    }
    @Override
    public User saveUser(UserInfoDto dto) throws IncorrectArgumentsException {
        if (!validateUser(dto)) throw new IncorrectArgumentsException("Incorrect data provided, unable to create an user");
        if (usersDao.findByUsername(dto.getUsername()) != null) throw new IncorrectArgumentsException("User with such username already exists");
        if (dto.getAuthorities() == null) {
            var roles = new ArrayList<org.example.entities.user.Role>();
            roles.add(rolesDao.findRoleByAuthority("ROLE_USER"));
            dto.setAuthorities(roles);
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Owner owner = null;
        if (dto.getOwnerId() != null) {
            owner = ownersDao.findById(dto.getOwnerId()).orElse(null);
        }
        return usersDao.save(new User(owner, dto.getUsername(), dto.getPassword(), dto.getAuthorities()));
    }

    @Override
    public User getUserById(long id) {
        return usersDao.findById(id);
    }
    @Override
    public User updateWithPasswordChange(UserInfoDto dto) throws IncorrectArgumentsException {
        if (!validateUser(dto) || dto.getId() == null) throw new IncorrectArgumentsException("Incorrect data provided, unable to update an user");
        if (dto.getAuthorities() == null) {
            var roles = new ArrayList<org.example.entities.user.Role>();
            roles.add(new org.example.entities.user.Role(Role.ROLE_USER.toString()));
            dto.setAuthorities(roles);
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        var owner = ownersDao.findById(dto.getOwnerId()).orElse(null);;
        return usersDao.save(new User(dto.getId(), owner, dto.getPassword(), dto.getUsername(), dto.getAuthorities()));
    }

    @Override
    public List<User> getUserByCriteria(FindCriteria criteria) {
        if (criteria.getRole() != null) return usersDao.findUserByAuthorities(criteria.getRole());
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return usersDao.findByUsername(username);
    }

    @Override
    public User updateRegularInfo(UserInfoDto dto) throws IncorrectArgumentsException {
        if (!validateUser(dto) || dto.getId() == null) throw new IncorrectArgumentsException("Incorrect data provided, unable to update an user");
        if (dto.getAuthorities() == null) {
            var roles = new ArrayList<org.example.entities.user.Role>();
            roles.add(new org.example.entities.user.Role(Role.ROLE_USER.toString()));
            dto.setAuthorities(roles);
        }
        var owner = ownersDao.findById(dto.getOwnerId()).orElse(null);;
        return usersDao.save(new User(dto.getId(), owner, dto.getPassword(), dto.getUsername(), dto.getAuthorities()));
    }

    @Override
    public void delete(long id) {
        usersDao.deleteById(id);
    }
}
