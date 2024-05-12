package org.example.user;
import lombok.RequiredArgsConstructor;
import org.example.entities.cat.Cat;
import org.example.entities.user.FindCriteria;
import org.example.entities.user.Role;
import org.example.entities.user.User;
import org.example.exceptions.IncorrectArgumentsException;
import org.example.infrastructure.UserIdentitySecurityChecker;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsersController {
    private final UserService service;
    private final AuthenticationManager authenticationManager;
    private final UserIdentitySecurityChecker securityChecker;

    @PostMapping("/user/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("Successfully logged in", HttpStatus.OK);
    }

    @PreAuthorize("#userId == authentication.principal.id" + "|| hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserInfoResponse> getUserById(@PathVariable long userId) {
        //if (!securityChecker.isAdmin() && !UserIdentitySecurityChecker.checkUserIdentity(userId)) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        var returnedUser = service.getUserById(userId);
        if (returnedUser == null) return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(new UserInfoResponse(returnedUser.getId(), returnedUser.getOwner().getId(), returnedUser.getUsername(), returnedUser.getAuthorities()),
                HttpStatus.OK);
    }
    @GetMapping("/users")
    public ResponseEntity<List<UserInfoResponse>> getUserByCriteria(@Param("role") String role) {
        var criteria = new FindCriteria(new Role(role));
        var returnedUser = service.getUserByCriteria(criteria);
        if (returnedUser == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(returnedUser.stream().map(x -> new UserInfoResponse(x.getId(), x.getOwner().getId(), x.getUsername(), x.getAuthorities())).toList(), HttpStatus.OK);
    }
    @PostMapping("/user")
    public ResponseEntity<UserIdResponse> save(@RequestBody UserInfoDto dto) {
        try {
            var returnedUser = service.saveUser(dto);
            return new ResponseEntity<>(new UserIdResponse(returnedUser.getId()),
                    HttpStatus.OK);
        }
        catch (IncorrectArgumentsException e) {
            return new ResponseEntity<>(new UserIdResponse(null),
                    HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("#userId == authentication.principal.id" + "|| hasRole('ROLE_ADMIN')")
    @PutMapping("/user/{userId}")
    public ResponseEntity<UserIdResponse> update(@RequestBody UserInfoDto dto, @PathVariable long userId) {
        //if (!securityChecker.isAdmin() && !UserIdentitySecurityChecker.checkUserIdentity(userId)) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        User returnedUser;
        try {
            returnedUser = service.update(dto);
        }
        catch (IncorrectArgumentsException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new UserIdResponse(returnedUser.getId()),
                HttpStatus.OK);
    }
    @PreAuthorize("#userId == authentication.principal.id" + "|| hasRole('ROLE_ADMIN')")
    @DeleteMapping("/user/{userId}")
    //if (!securityChecker.isAdmin() && !UserIdentitySecurityChecker.checkUserIdentity(userId)) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    public void delete(@PathVariable long userId) {
        service.delete(userId);
    }
}
