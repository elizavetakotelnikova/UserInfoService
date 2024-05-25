package com.example.catmicroservice.services;

import com.example.catmicroservice.exceptions.IncorrectArgumentsException;
import com.example.catmicroservice.valueObjects.FindCriteria;
import com.example.jpa.CatDto;

import java.util.List;
public interface CatService {
    CatDto saveCat(CatDto dto) throws IncorrectArgumentsException;
    //boolean checkIfCatsAreFriends(long firstId, long secondId);
    List<CatDto> getCatByCriteria(FindCriteria criteria);
    CatDto getCatById(long id);
    void delete(long id);
    CatDto update(CatDto dto) throws IncorrectArgumentsException;
}
