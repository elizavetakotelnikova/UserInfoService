package org.example.cat;
import lombok.RequiredArgsConstructor;
import org.example.cat.dto.CatCreateResponse;
import org.example.cat.dto.CatIdResponse;
import org.example.entities.cat.Cat;
import org.example.entities.cat.FindCriteria;
import org.example.exceptions.IncorrectArgumentsException;
import org.example.security.ContextManager;
import org.example.security.UserIdentitySecurityChecker;
import org.example.user.UserService;
import org.example.valueObjects.Color;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CatsController {
    private final CatService service;
    private final FriendUsecases friendUsecases;
    private final UserService userService;
    private final UserIdentitySecurityChecker securityChecker;
    private final ContextManager contextManager;
    @GetMapping("/cat/{catId}")
    public ResponseEntity<CatCreateResponse> getCatById(@PathVariable long catId) {
        if (!securityChecker.isAdmin() && !securityChecker.checkContextOwnerForCat(catId)) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
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
        List<Cat> allowedCats;
        if (securityChecker.isAdmin()) allowedCats = returnedCats;
        else {
            allowedCats = returnedCats.stream().filter(x -> {
                    try {
                        securityChecker.checkContextOwnerForCat(x);
                    }
                    catch (AccessDeniedException exc) {
                        return false;
                    }
                    return true;
            }).toList();
        }
        return new ResponseEntity<>(allowedCats.stream().map(x -> new CatInfoDto(x.getId(), x.getName(), x.getBreed(),
                x.getColor(), x.getOwner().getId(), x.getBirthday(), x.getFriends().stream().map(y -> x.getId()).toList())).toList(), HttpStatus.OK);
    }
    @PostMapping("/cat")
    public ResponseEntity<CatIdResponse> save(@RequestBody CatInfoDto dto) {
        if (!contextManager.setCurrentOwner(dto)) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        if (!securityChecker.isAdmin() && !securityChecker.checkIsTheContextOwner(dto.getOwnerId())) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
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
        if (!securityChecker.isAdmin() && !securityChecker.checkContextOwnerForCat(catId)) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        if (dto.getId() == null) dto.setId(catId);
        if (!contextManager.setCurrentOwner(dto)) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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
        if (!securityChecker.isAdmin() && !securityChecker.checkContextOwnerForCat(catId)) throw new AccessDeniedException("Access denied");
        service.delete(catId);
    }
}

