package org.example;
import lombok.AllArgsConstructor;
import org.example.entities.cat.Cat;
import org.example.entities.owner.FindCriteria;
import org.example.entities.owner.Owner;
import org.example.exceptions.IncorrectArgumentsException;
import org.example.owner.ManagingCatsUsecases;
import org.example.owner.OwnerService;
import org.example.owner.ownerSavingDto;

import java.util.List;

@AllArgsConstructor
public class OwnersController {
    private OwnerService service;
    private ManagingCatsUsecases managingCatsUsecases;
    public Owner getOwnerById(long id) {
        return service.getOwnerById(id);
    }
    public List<Owner> getOwnerByCriteria(FindCriteria criteria) {
        return service.getOwnerByCriteria(criteria);
    }
    public Owner save(ownerSavingDto dto) throws IncorrectArgumentsException {
        return service.saveOwner(dto);
    }
    //add newCat
    public void delete(long id) {
        service.delete(id);
    }
}
