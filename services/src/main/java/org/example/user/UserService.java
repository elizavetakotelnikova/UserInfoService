package org.example.user;

import org.example.entities.user.FindCriteria;
import org.example.entities.user.User;
import org.example.exceptions.IncorrectArgumentsException;

import java.util.List;

public interface UserService {
    User saveUser(UserInfoDto dto) throws IncorrectArgumentsException;
    User getUserById(long id);
    User updateWithPasswordChange(UserInfoDto dto) throws IncorrectArgumentsException;
    List<User> getUserByCriteria(FindCriteria criteria);
    User getUserByUsername(String username);
    User updateRegularInfo(UserInfoDto dto) throws IncorrectArgumentsException;
    void delete(long id);
}
