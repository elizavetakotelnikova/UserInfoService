package org.example;

import lombok.AllArgsConstructor;
import org.example.cat.CatService;
import org.example.cat.catSavingDto;
import org.example.entities.cat.Cat;
import org.example.entities.cat.FindCriteria;
import org.example.exceptions.IncorrectArgumentsException;

import java.util.List;

@AllArgsConstructor
public class CatsController {
    private CatService service;
    public Cat getCatById(long id) {
        return service.getCatById(id);
    }
    public List<Cat> getCatByCriteria(FindCriteria criteria) {
        return service.getCatByCriteria(criteria);
    }
    public Cat save(catSavingDto dto) throws IncorrectArgumentsException {
        return service.saveCat(dto);
    }
    /*public void friendCats(long firstCatId, long secondCatId) {
        service.friendCats(firstCatId, secondCatId);
    }*/
    public void delete(long id) {
        service.delete(id);
    }
}
