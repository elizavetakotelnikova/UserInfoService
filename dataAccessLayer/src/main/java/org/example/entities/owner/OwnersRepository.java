package org.example.entities.owner;

import java.util.List;

public interface OwnersRepository {
    Owner create(Owner owner);
    Owner update(Owner owner);
    Owner getById(long id);
    List<Owner> getByCriteria(FindCriteria criteria);
    void delete(Owner owner);
}
