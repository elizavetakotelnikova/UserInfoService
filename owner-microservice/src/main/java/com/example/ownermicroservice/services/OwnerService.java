package com.example.ownermicroservice.services;

import com.example.ownermicroservice.exceptions.IncorrectArgumentsException;
import com.example.ownermicroservice.models.OwnerInfoDto;
import com.example.jpa.Owner;
import com.example.ownermicroservice.repositories.FindCriteria;

import java.util.List;

public interface OwnerService {
    Owner saveOwner(OwnerInfoDto dto) throws IncorrectArgumentsException;
    //boolean checkIfOwnersAreFriends(long firstId, long secondId);
    List<Owner> getOwnerByCriteria(FindCriteria criteria);
    Owner getOwnerById(long id);
    Owner update(OwnerInfoDto dto) throws IncorrectArgumentsException;
    void delete(long id);
}
