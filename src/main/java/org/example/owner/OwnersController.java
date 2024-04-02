package org.example.owner;
import org.example.entities.cat.Cat;
import org.example.entities.owner.FindCriteria;
import org.example.exceptions.IncorrectArgumentsException;
import org.example.owner.responseModels.OwnerCreateResponse;
import org.example.owner.responseModels.OwnerIdResponse;
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
    @Autowired
    public OwnersController(OwnerService ownerService) {
        service = ownerService;
    }
    @Autowired
    private ManagingCatsUsecases managingCatsUsecases;
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<OwnerCreateResponse> getOwnerById(long id) {
        var returnedOwner = service.getOwnerById(id);
        return new ResponseEntity<>(new OwnerCreateResponse(returnedOwner.getId(), returnedOwner.getBirthday(), returnedOwner.getCats().stream().map(Cat::getId).toList()),
                HttpStatus.OK);
    }
    @GetMapping("/owners")
    public ResponseEntity<List<OwnerInfoDto>> getOwnerByCriteria(@Param("friend") Long catId,
                                             @Param("birthday") LocalDate birthday) {
        var criteria = new FindCriteria(birthday);
        var returnedOwner = service.getOwnerByCriteria(criteria);
        return new ResponseEntity<>(returnedOwner.stream().map(x -> new OwnerInfoDto(x.getId(), x.getBirthday(), x.getCats())).toList(), HttpStatus.OK);
    }
    @PostMapping("/owner")
    public ResponseEntity<OwnerIdResponse> save(@RequestBody OwnerInfoDto dto) throws IncorrectArgumentsException {
        var returnedOwner = service.saveOwner(dto);
        return new ResponseEntity<>(new OwnerIdResponse(returnedOwner.getId()),
                HttpStatus.OK);
    }
    @PutMapping("/owner/{ownerId}")
    public ResponseEntity<OwnerIdResponse> update(@RequestBody OwnerInfoDto dto) throws IncorrectArgumentsException {
        var returnedOwner = service.update(dto);
        return new ResponseEntity<>(new OwnerIdResponse(returnedOwner.getId()),
                HttpStatus.OK);
    }
    @DeleteMapping("/owner/{ownerId}")
    public void delete(long id) {
        service.delete(id);
    }
}
