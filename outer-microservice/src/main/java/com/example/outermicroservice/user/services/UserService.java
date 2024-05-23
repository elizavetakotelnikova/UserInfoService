package com.example.outermicroservice.user.services;
import com.example.outermicroservice.exceptions.IncorrectArgumentsException;
import com.example.outermicroservice.user.FindCriteria;
import com.example.outermicroservice.user.UserInfoDto;
import com.example.jpa.User;

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