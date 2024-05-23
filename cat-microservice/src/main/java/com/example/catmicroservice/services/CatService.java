package com.example.catmicroservice.services;

import com.example.catmicroservice.exceptions.IncorrectArgumentsException;
import com.example.catmicroservice.models.CatInfoDto;
import com.example.catmicroservice.valueObjects.FindCriteria;
import com.example.jpa.Cat;

import java.util.List;
public interface CatService {
    Cat saveCat(CatInfoDto dto) throws IncorrectArgumentsException;
    //boolean checkIfCatsAreFriends(long firstId, long secondId);
    List<Cat> getCatByCriteria(FindCriteria criteria);
    Cat getCatById(long id);
    void delete(long id);
    Cat update(CatInfoDto dto) throws IncorrectArgumentsException;
}
