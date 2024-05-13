package org.example.owner;
import lombok.RequiredArgsConstructor;
import org.example.entities.cat.Cat;
import org.example.entities.owner.FindCriteria;
import org.example.entities.owner.Owner;
import org.example.exceptions.IncorrectArgumentsException;
import org.example.infrastructure.UserIdentitySecurityChecker;
import org.example.owner.dto.OwnerIdResponse;
import org.example.owner.dto.OwnerInfoResponse;
import org.example.user.UserInfoDto;
import org.example.user.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OwnersController {
    private final OwnerService service;
    private final ManagingCatsUsecases managingCatsUsecases;
    private final UserIdentitySecurityChecker securityChecker;
    private final UserService usersService;
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<OwnerInfoResponse> getOwnerById(@PathVariable long ownerId) {
        /*if (!securityChecker.isAdmin() && !securityChecker.checkIsTheContextOwner(ownerId)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }*/
        var returnedOwner = service.getOwnerById(ownerId);
        if (returnedOwner == null) return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(new OwnerInfoResponse(returnedOwner.getId(), returnedOwner.getBirthday(), returnedOwner.getCats().stream().map(Cat::getId).toList()),
                HttpStatus.OK);
    }
    @GetMapping("/owners")
    public ResponseEntity<List<OwnerInfoResponse>> getOwnerByCriteria(@Param("friend") Long catId,
                                                                      @Param("birthday") LocalDate birthday) {
        var criteria = new FindCriteria(birthday);
        var returnedOwner = service.getOwnerByCriteria(criteria);
        if (returnedOwner == null) return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(returnedOwner.stream().map(x -> new OwnerInfoResponse(x.getId(), x.getBirthday(), x.getCats().stream().map(Cat::getId).toList())).toList(), HttpStatus.OK);
    }
    @PostMapping("/owner")
    public ResponseEntity<OwnerIdResponse> save(@RequestBody OwnerInfoDto dto) {
        try {
            var returnedOwner = service.saveOwner(dto);
            var user = usersService.getUserById(securityChecker.getUserId());
            user.setOwner(returnedOwner);
            usersService.updateRegularInfo(new UserInfoDto(user.getId(), user.getUsername(), user.getPassword(), returnedOwner.getId(), user.getAuthorities()));
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
        if (!securityChecker.isAdmin() && !securityChecker.checkIsTheContextOwner(ownerId)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        Owner returnedOwner;
        if (dto.getId() == null) dto.setId(ownerId);
        try {
            returnedOwner = service.update(dto);
        }
        catch (IncorrectArgumentsException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new OwnerIdResponse(returnedOwner.getId()),
                HttpStatus.OK);
    }
    @DeleteMapping("/owner/{ownerId}")
    public void delete(@PathVariable long ownerId) {
        if (!securityChecker.isAdmin() && !securityChecker.checkIsTheContextOwner(ownerId)) throw new AccessDeniedException("Access denied");
        service.delete(ownerId);
    }
}
