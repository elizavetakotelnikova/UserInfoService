package org.example.owner;
import lombok.RequiredArgsConstructor;
import org.example.entities.cat.Cat;
import org.example.entities.owner.FindCriteria;
import org.example.entities.owner.Owner;
import org.example.exceptions.IncorrectArgumentsException;
import org.example.infrastructure.UserIdentitySecurityChecker;
import org.example.owner.responseModels.JwtAuthToken;
import org.example.owner.responseModels.OwnerCreateResponse;
import org.example.owner.responseModels.OwnerIdResponse;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OwnersController {
    private final OwnerService service;
    private final ManagingCatsUsecases managingCatsUsecases;
    /*@PostMapping("/owner/token")
    public ResponseEntity<JwtAuthToken> getToken(@RequestBody UserLoginInfo userLoginInfo) {
        var token = service.getToken(userLoginInfo.getUsername(), userLoginInfo.getPassword());
        if (token == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new JwtAuthToken(token), HttpStatus.OK);
    }*/

    @PreAuthorize("#ownerId == authentication.principal.id")
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<OwnerCreateResponse> getOwnerById(@PathVariable long ownerId) {
        //if (!UserIdentitySecurityChecker.checkUserIdentity(ownerId)) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
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
        return new ResponseEntity<>(returnedOwner.stream().map(x -> new OwnerInfoDto(x.getId(), x.getBirthday(), x.getCats(), x.getPassword().toString(), x.getUsername(), x.getAuthorities())).toList(), HttpStatus.OK);
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
    @PreAuthorize("#ownerId == authentication.principal.id")
    @PutMapping("/owner/{ownerId}")
    public ResponseEntity<OwnerIdResponse> update(@RequestBody OwnerInfoDto dto, @PathVariable long ownerId) {
        Owner returnedOwner;
        try {
            returnedOwner = service.update(dto);
        }
        catch (IncorrectArgumentsException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new OwnerIdResponse(returnedOwner.getId()),
                HttpStatus.OK);
    }
    @PreAuthorize("#ownerId == authentication.principal.id")
    @DeleteMapping("/owner/{ownerId}")
    public void delete(@PathVariable long ownerId) {
        service.delete(ownerId);
    }
}
