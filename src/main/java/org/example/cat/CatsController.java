package org.example.cat;
import org.example.cat.responseModels.CatCreateResponse;
import org.example.cat.responseModels.CatIdResponse;
import org.example.entities.cat.Cat;
import org.example.entities.cat.FindCriteria;
import org.example.exceptions.IncorrectArgumentsException;
import org.example.valueObjects.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class CatsController {
    private final CatService service;
    @Autowired
    public CatsController(CatService catService) {
        service = catService;
    }
    //private FriendUsecases friendUsecases;
    @GetMapping("/cat/{catId}")
    public ResponseEntity<CatCreateResponse> getCatById(long id) {
        var returnedCat = service.getCatById(id);
        return new ResponseEntity<>(new CatCreateResponse(returnedCat.getId(), returnedCat.getName(),
                returnedCat.getBreed(), returnedCat.getColor(), returnedCat.getOwner().getId(), returnedCat.getBirthday(), returnedCat.getFriends().stream().map(Cat::getId).toList()), HttpStatus.OK);
    }
    @GetMapping("/cats")
    public ResponseEntity<List<CatInfoDto>> getCatByCriteria(@Param("name") String name, @Param("breed") String breed,
                                             @Param("ownerId") Long ownerId, @Param("color") Color color,
                                             @Param("birthday") LocalDate birthday) {
        var criteria = new FindCriteria(name, breed, color, ownerId, birthday);
        var returnedCats = service.getCatByCriteria(criteria);
        return new ResponseEntity<>(returnedCats.stream().map(x -> new CatInfoDto(x.getId(), x.getName(), x.getBreed(),
                x.getColor(), x.getId(), x.getBirthday(), x.getFriends().stream().map(y -> x.getId()).toList())).toList(), HttpStatus.OK);
    }
    @PostMapping("/cat")
    public ResponseEntity<CatIdResponse> save(@RequestBody CatInfoDto dto) throws IncorrectArgumentsException {
        var returnedCat = service.saveCat(dto);
        return new ResponseEntity<>(new CatIdResponse(returnedCat.getId()), HttpStatus.OK);
    }
    @PutMapping("/cat/{catId}")
    public ResponseEntity<CatIdResponse> update(@RequestBody CatInfoDto dto) throws IncorrectArgumentsException {
        var updatedCat = service.update(dto);
        return new ResponseEntity<>(new CatIdResponse(updatedCat.getId()), HttpStatus.OK);
    }
    @DeleteMapping("/cat/{catId}")
    public void delete(long id) {
        service.delete(id);
    }
}

