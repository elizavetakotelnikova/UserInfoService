package org.example.entities.cat;

import java.util.List;

public interface CatsRepository {
    Cat create(Cat cat);
    Cat update(Cat cat);
    Cat getById(long id);
    List<Cat> getByCriteria(FindCriteria criteria);
    void delete(Cat cat);
}
