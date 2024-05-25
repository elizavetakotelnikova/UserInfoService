package com.example.ownermicroservice.services;

import com.example.jpa.OwnerDto;
import com.example.ownermicroservice.exceptions.IncorrectArgumentsException;
import com.example.ownermicroservice.models.OwnerInfoDto;
import com.example.jpa.Owner;
import com.example.ownermicroservice.repositories.FindCriteria;

import java.util.List;

public interface OwnerService {
    OwnerDto saveOwner(OwnerDto dto) throws IncorrectArgumentsException;
    //boolean checkIfOwnersAreFriends(long firstId, long secondId);
    List<OwnerDto> getOwnerByCriteria(FindCriteria criteria);
    OwnerDto getOwnerById(long id);
    OwnerDto update(OwnerDto dto) throws IncorrectArgumentsException;
    void delete(long id);
}
