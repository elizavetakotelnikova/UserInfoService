package org.example.cat;

import org.example.entities.cat.Cat;
import org.example.entities.cat.FindCriteria;
import org.example.exceptions.IncorrectArgumentsException;

import java.util.List;
public interface CatService {
    Cat saveCat(CatInfoDto dto) throws IncorrectArgumentsException;
    //boolean checkIfCatsAreFriends(long firstId, long secondId);
    List<Cat> getCatByCriteria(FindCriteria criteria);
    Cat getCatById(long id);
    void delete(long id);
    Cat update(CatInfoDto dto) throws IncorrectArgumentsException;
}
