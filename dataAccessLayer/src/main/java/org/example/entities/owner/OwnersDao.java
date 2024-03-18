package org.example.entities.owner;

import java.util.List;

public interface OwnersDao {
    Owner save(Owner owner);
    Owner update(Owner owner);
    List<Owner> findAll();
    Owner findById(long id);
    List<Owner> findByCriteria(FindCriteria criteria);
    void delete(long id);
}
