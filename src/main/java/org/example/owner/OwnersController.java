package org.example.owner;
import org.example.entities.cat.Cat;
import org.example.entities.owner.FindCriteria;
import org.example.entities.owner.Owner;
import org.example.exceptions.IncorrectArgumentsException;
import org.example.owner.responseModels.OwnerCreateResponse;
import org.example.owner.responseModels.OwnerIdResponse;
import org.example.owner.responseModels.OwnersSavingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class OwnersController {
    private final OwnerService service;
    private final ManagingCatsUsecases managingCatsUsecases;
    @Autowired
    public OwnersController(OwnerService ownerService, ManagingCatsUsecases managingCatsUsecases) {
        service = ownerService;
        this.managingCatsUsecases = managingCatsUsecases;

    }
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<OwnerCreateResponse> getOwnerById(@PathVariable long ownerId) {
        var returnedOwner = service.getOwnerById(ownerId);
        if (returnedOwner == null) return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(new OwnerCreateResponse(returnedOwner.getId(), returnedOwner.getBirthday(), returnedOwner.getCats().stream().map(Cat::getId).toList()),
                HttpStatus.OK);
    }
    @GetMapping("/owners")
    public ResponseEntity<List<OwnerInfoDto>> getOwnerByCriteria(@Param("friend") Long catId,
                                             @Param("birthday") LocalDate birthday) {
        var criteria = new FindCriteria(birthday);
        var returnedOwner = service.getOwnerByCriteria(criteria);
        if (returnedOwner == null) return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(returnedOwner.stream().map(x -> new OwnerInfoDto(x.getId(), x.getBirthday(), x.getCats())).toList(), HttpStatus.OK);
    }
    @PostMapping("/owner")
    public ResponseEntity<OwnerIdResponse> save(@RequestBody OwnerInfoDto dto) {
        try {
            var returnedOwner = service.saveOwner(dto);
            return new ResponseEntity<>(new OwnerIdResponse(returnedOwner.getId()),
                    HttpStatus.OK);
        }
        catch (IncorrectArgumentsException e) {
            return new ResponseEntity<>(new OwnerIdResponse(null),
                    HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/owner/{ownerId}")
    public ResponseEntity<OwnerIdResponse> update(@RequestBody OwnerInfoDto dto, @PathVariable long ownerId) {
        Owner returnedOwner;
        try {
            returnedOwner = service.update(dto);
        }
        catch (IncorrectArgumentsException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new OwnerIdResponse(returnedOwner.getId()),
                HttpStatus.OK);
    }
    @DeleteMapping("/owner/{ownerId}")
    public void delete(@PathVariable long ownerId) {
        service.delete(ownerId);
    }
}
