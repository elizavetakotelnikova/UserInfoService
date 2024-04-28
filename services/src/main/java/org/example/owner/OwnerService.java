package org.example.owner;
import org.example.entities.owner.Owner;
import org.example.entities.owner.FindCriteria;
import org.example.exceptions.IncorrectArgumentsException;

import java.util.List;

public interface OwnerService {
    Owner saveOwner(OwnerInfoDto dto) throws IncorrectArgumentsException;
    //boolean checkIfOwnersAreFriends(long firstId, long secondId);
    List<Owner> getOwnerByCriteria(FindCriteria criteria);
    Owner getOwnerById(long id);
    Owner update(OwnerInfoDto dto) throws IncorrectArgumentsException;
    Owner getOwnerByUsername(String username);
    String getToken(String username, String password);
    void delete(long id);
}
