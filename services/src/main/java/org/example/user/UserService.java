package org.example.user;

import org.example.entities.user.FindCriteria;
import org.example.entities.user.User;
import org.example.exceptions.IncorrectArgumentsException;

import java.util.List;

public interface UserService {
    User saveUser(UserInfoDto dto) throws IncorrectArgumentsException;
    User getUserById(long id);
    User update(UserInfoDto dto) throws IncorrectArgumentsException;
    List<User> getUserByCriteria(FindCriteria criteria);
    User getUserByUsername(String username);
    String getToken(String username, String password);
    void delete(long id);
}
