package org.example.owner;

import lombok.AllArgsConstructor;
import org.example.entities.cat.CatsDao;
import org.example.entities.owner.Owner;
import org.example.entities.owner.OwnersDao;
import org.example.entities.owner.FindCriteria;
import org.example.exceptions.IncorrectArgumentsException;
import java.util.List;

@AllArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private CatsDao catsDao;
    private OwnersDao ownersDao;
    @Override
    public Owner saveOwner(ownerSavingDto dto) throws IncorrectArgumentsException {
        if (dto.getBirthday() != null) {
            var cats = dto.getCats().stream().map(catsDao::findById).toList();
            return ownersDao.save(new Owner(dto.getBirthday(), cats));
        }
        throw new IncorrectArgumentsException("Incorrect data provided, unable to save a cat");
    }

    @Override
    public List<Owner> getOwnerByCriteria(FindCriteria criteria) {
        return ownersDao.findByCriteria(criteria);
    }

    @Override
    public Owner getOwnerById(long id) {
        return ownersDao.findById(id);
    }
    @Override
    public Owner update(Owner owner) {
        return ownersDao.update(owner);
    }
    @Override
    public void delete(long id) {
        ownersDao.delete(id);
    }
}
