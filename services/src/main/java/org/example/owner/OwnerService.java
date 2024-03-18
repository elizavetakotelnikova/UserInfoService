package org.example.owner;
import org.example.entities.owner.Owner;
import org.example.entities.owner.FindCriteria;
import org.example.exceptions.IncorrectArgumentsException;

import java.util.List;

public interface OwnerService {
    Owner saveOwner(ownerSavingDto dto) throws IncorrectArgumentsException;
    //boolean checkIfOwnersAreFriends(long firstId, long secondId);
    List<Owner> getOwnerByCriteria(FindCriteria criteria);
    Owner getOwnerById(long id);
    Owner update(Owner owner);
    void delete(long id);
}
