package org.example.entities.cat;

import java.util.List;

public interface CatsDao {
    List<Cat> getAll();
    Cat findById(long id);
    Cat update(Cat cat);
    List<Cat> findByCriteria(FindCriteria criteria);
    Cat save(Cat cat);
    void deleteById(long id);

}
