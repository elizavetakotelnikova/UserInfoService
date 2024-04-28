package org.example.cat;
import lombok.RequiredArgsConstructor;
import org.example.cat.dto.CatCreateResponse;
import org.example.cat.dto.CatIdResponse;
import org.example.cat.dto.CatSavingRequest;
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
@RequiredArgsConstructor
public class CatsController {
    private final CatService service;
    private final FriendUsecases friendUsecases;
    @GetMapping("/cat/{catId}")
    public ResponseEntity<CatCreateResponse> getCatById(@PathVariable long catId) {
        var returnedCat = service.getCatById(catId);
        if (returnedCat == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new CatCreateResponse(returnedCat.getId(), returnedCat.getName(),
                returnedCat.getBreed(), returnedCat.getColor(), returnedCat.getOwner().getId(), returnedCat.getBirthday(), returnedCat.getFriends().stream().map(Cat::getId).toList()), HttpStatus.OK);
    }
    @GetMapping("/cats")
    public ResponseEntity<List<CatInfoDto>> getCatByCriteria(@Param("name") String name, @Param("breed") String breed,
                                             @Param("ownerId") Long ownerId, @Param("color") Color color,
                                             @Param("birthday") LocalDate birthday) {
        var criteria = new FindCriteria(name, breed, color, ownerId, birthday);
        var returnedCats = service.getCatByCriteria(criteria);
        if (returnedCats == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(returnedCats.stream().map(x -> new CatInfoDto(x.getId(), x.getName(), x.getBreed(),
                x.getColor(), x.getId(), x.getBirthday(), x.getFriends().stream().map(y -> x.getId()).toList())).toList(), HttpStatus.OK);
    }
    @PostMapping("/cat")
    public ResponseEntity<CatIdResponse> save(@RequestBody CatInfoDto dto) {
        Cat returnedCat;
        try {




            returnedCat = service.saveCat(dto);
        }
        catch (IncorrectArgumentsException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new CatIdResponse(returnedCat.getId()), HttpStatus.OK);
    }
    @PutMapping("/cat/{catId}")
    public ResponseEntity<CatIdResponse> update(@RequestBody CatInfoDto dto, @PathVariable long catId) {
        Cat updatedCat;
        try {
            updatedCat = service.update(dto);
        }
        catch (IncorrectArgumentsException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new CatIdResponse(updatedCat.getId()), HttpStatus.OK);
    }
    @DeleteMapping("/cat/{catId}")
    public void delete(@PathVariable long catId) {
        service.delete(catId);
    }
}

