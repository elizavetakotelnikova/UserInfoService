package com.example.outermicroservice.user.services;

import com.example.jpa.Role;
import com.example.jpa.User;
import com.example.outermicroservice.exceptions.IncorrectArgumentsException;
import com.example.outermicroservice.role.RoleEnum;
import com.example.outermicroservice.role.RolesDao;
import com.example.outermicroservice.user.FindCriteria;
import com.example.outermicroservice.user.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UsersDao usersDao;
    private final RolesDao rolesDao;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(UsersDao usersDao, RolesDao rolesDao, PasswordEncoder passwordEncoder) {
        this.usersDao = usersDao;
        this.rolesDao = rolesDao;
        this.passwordEncoder = passwordEncoder;
    }
    private boolean validateUser(UserInfoDto dto) {
        if (dto.getUsername() == null) return false;
        if (dto.getPassword() == null) return false;
        return true;
    }
    @Override
    public User saveUser(UserInfoDto dto) throws IncorrectArgumentsException {
        if (!validateUser(dto)) throw new IncorrectArgumentsException("Incorrect data provided, unable to create an user");
        if (usersDao.findByUsername(dto.getUsername()) != null) throw new IncorrectArgumentsException("User with such username already exists");
        if (dto.getAuthorities() == null) {
            var roles = new ArrayList<Role>();
            roles.add(rolesDao.findRoleByAuthority("ROLE_USER"));
            dto.setAuthorities(roles);
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        // два варианта, с созданием владельца и без
        return usersDao.save(new User(dto.getUsername(), dto.getPassword(), dto.getAuthorities()));
    }

    @Override
    public User getUserById(long id) {
        return usersDao.findById(id);
    }
    @Override
    public User updateWithPasswordChange(UserInfoDto dto) throws IncorrectArgumentsException {
        if (!validateUser(dto) || dto.getId() == null) throw new IncorrectArgumentsException("Incorrect data provided, unable to update an user");
        if (dto.getAuthorities() == null) {
            var roles = new ArrayList<Role>();
            roles.add(new Role(RoleEnum.ROLE_USER.toString()));
            dto.setAuthorities(roles);
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return usersDao.save(new User(dto.getId(), dto.getPassword(), dto.getUsername(), dto.getAuthorities()));
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
            var roles = new ArrayList<Role>();
            roles.add(new Role(RoleEnum.ROLE_USER.toString()));
            dto.setAuthorities(roles);
        }
        return usersDao.save(new User(dto.getId(), dto.getPassword(), dto.getUsername(), dto.getAuthorities()));
    }

    @Override
    public void delete(long id) {
        usersDao.deleteById(id);
    }
}
