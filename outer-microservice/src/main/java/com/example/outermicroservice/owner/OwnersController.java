package com.example.outermicroservice.owner;

import com.example.jpa.Cat;
import com.example.outermicroservice.rabbitMQ.RabbitMQConfig;
import com.example.jpa.User;
import com.example.outermicroservice.exceptions.IncorrectArgumentsException;
import com.example.outermicroservice.owner.dto.OwnerIdResponse;
import com.example.outermicroservice.owner.dto.OwnerInfoDto;
import com.example.outermicroservice.owner.dto.OwnerInfoResponse;
import com.example.jpa.OwnerDto;
import com.example.outermicroservice.security.UserIdentitySecurityChecker;
import com.example.outermicroservice.owner.dto.FindCriteria;
import com.example.outermicroservice.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OwnersController {
    private final RabbitTemplate rabbitTemplate;
    private final UserIdentitySecurityChecker securityChecker;
    private final UserService usersService;
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<OwnerInfoResponse> getOwnerById(@PathVariable long ownerId) {
        if (!securityChecker.isAdmin() && !securityChecker.checkIsTheContextOwner(ownerId)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        var returnedOwner = (OwnerDto) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.WEB_EXCHANGE, RabbitMQConfig.ROUTING_KEY_FINDING_OWNER, ownerId);
        if (returnedOwner == null) return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(new OwnerInfoResponse(returnedOwner.getId(), returnedOwner.getBirthday(), returnedOwner.getCats().stream().map(Cat::getId).toList(),
                returnedOwner.getUser().getId()),
                HttpStatus.OK);
    }
    @GetMapping("/owners")
    public ResponseEntity<List<OwnerInfoResponse>> getOwnerByCriteria(@Param("friend") Long catId,
                                                                      @Param("birthday") LocalDate birthday, @Param("userId") Long userId) {
        User user = null;
        if (userId != null) user = usersService.getUserById(userId);
        var criteria = new FindCriteria(birthday, user);
        var returnedOwner = (List<OwnerDto>) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.WEB_EXCHANGE, RabbitMQConfig.ROUTING_KEY_FINDING_OWNER_BY_CRITERIA, criteria);
        if (returnedOwner == null || returnedOwner.isEmpty()) return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(returnedOwner.stream().map(x -> new OwnerInfoResponse(x.getId(), x.getBirthday(), x.getCats().stream().map(Cat::getId).toList(), x.getUser().getId())).toList(), HttpStatus.OK);
    }
    @PostMapping("/owner")
    public ResponseEntity<OwnerIdResponse> save(@RequestBody OwnerInfoDto dto) {
        try {
            if (dto.getUserId() == null) dto.setUserId(securityChecker.getUserId());
            var returnedUser = usersService.getUserById(dto.getUserId());
            var messagingDto = new OwnerDto(dto.getId(), dto.getBirthday(), dto.getCats(), returnedUser);
            var owner = (OwnerDto) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.WEB_EXCHANGE, RabbitMQConfig.ROUTING_KEY_CREATING_OWNER, messagingDto);
            if (owner == null) throw new IncorrectArgumentsException("Incorrect data provided");
            return new ResponseEntity<>(new OwnerIdResponse(owner.getId()),
                    HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null,
                    HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/owner/{ownerId}")
    public ResponseEntity<OwnerIdResponse> update(@RequestBody OwnerInfoDto dto, @PathVariable long ownerId) {
        if (!securityChecker.isAdmin() && !securityChecker.checkIsTheContextOwner(ownerId)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (dto.getUserId() == null) dto.setUserId(securityChecker.getUserId());
        if (dto.getId() == null) dto.setId(ownerId);
        var returnedUser = usersService.getUserById(dto.getUserId());
        var messagingDto = new OwnerDto(dto.getId(), dto.getBirthday(), dto.getCats(), returnedUser);
        rabbitTemplate.convertAndSend(RabbitMQConfig.WEB_EXCHANGE, RabbitMQConfig.ROUTING_KEY_UPDATING_OWNER, messagingDto);
        return new ResponseEntity<>(null,
                HttpStatus.OK);
    }
    @DeleteMapping("/owner/{ownerId}")
    public void delete(@PathVariable long ownerId) {
        if (!securityChecker.isAdmin() && !securityChecker.checkIsTheContextOwner(ownerId)) throw new AccessDeniedException("Access denied");
        rabbitTemplate.convertAndSend(RabbitMQConfig.DELETING_OWNER_QUEUE, ownerId);
    }
}
