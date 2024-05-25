package com.example.outermicroservice.cat;
import com.example.jpa.CatDto;
import com.example.jpa.Color;
import com.example.jpa.RabbitMQConfig;
import com.example.outermicroservice.cat.dto.CatCreateResponse;
import com.example.outermicroservice.cat.dto.CatIdResponse;
import com.example.outermicroservice.cat.dto.CatInfoDto;
import com.example.outermicroservice.security.ContextManager;
import com.example.outermicroservice.security.UserIdentitySecurityChecker;
import com.example.outermicroservice.cat.dto.FindCriteria;
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
public class CatsController {
    private final RabbitTemplate rabbitTemplate;
    private final UserIdentitySecurityChecker securityChecker;
    private final ContextManager contextManager;
    @GetMapping("/cat/{catId}")
    public ResponseEntity<CatCreateResponse> getCatById(@PathVariable long catId) {
        if (!securityChecker.isAdmin() && !securityChecker.checkContextOwnerForCat(catId)) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        var returnedCat = (CatDto) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.WEB_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_CREATING_CAT, catId);
        if (returnedCat == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new CatCreateResponse(returnedCat.getId(), returnedCat.getName(),
                returnedCat.getBreed(), returnedCat.getColor(), returnedCat.getOwner().getId(), returnedCat.getBirthday(), returnedCat.getFriendsId()), HttpStatus.OK);
    }
    @GetMapping("/cats")
    public ResponseEntity<List<CatInfoDto>> getCatByCriteria(@Param("name") String name, @Param("breed") String breed,
                                                             @Param("ownerId") Long ownerId, @Param("color") Color color,
                                                             @Param("birthday") LocalDate birthday) {
        var criteria = new FindCriteria(name, breed, color, ownerId, birthday);
        var returnedCats = (List<CatDto>) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.WEB_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_FINDING_CAT_BY_CRITERIA, criteria);
        if (returnedCats == null || returnedCats.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        List<CatDto> allowedCats;
        if (securityChecker.isAdmin()) allowedCats = returnedCats;
        else {
            allowedCats = returnedCats.stream().filter(x -> {
                    try {
                        securityChecker.checkContextOwnerForCat(x.getId());
                    }
                    catch (AccessDeniedException exc) {
                        return false;
                    }
                    return true;
            }).toList();
        }
        return new ResponseEntity<>(allowedCats.stream().map(x -> new CatInfoDto(x.getId(), x.getName(), x.getBreed(),
                x.getColor(), x.getOwner().getId(), x.getBirthday(), x.getFriendsId().stream().map(y -> x.getId()).toList())).toList(), HttpStatus.OK);
    }
    @PostMapping("/cat")
    public ResponseEntity<CatIdResponse> save(@RequestBody CatInfoDto dto) {
        if (!contextManager.setCurrentOwner(dto)) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        if (!securityChecker.isAdmin() && !securityChecker.checkIsTheContextOwner(dto.getOwnerId()))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        CatDto returnedCat;
        try {
            returnedCat = (CatDto) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.WEB_EXCHANGE,
                    RabbitMQConfig.ROUTING_KEY_CREATING_CAT, dto);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new CatIdResponse(returnedCat.getId()), HttpStatus.OK);
    }
    @PutMapping("/cat/{catId}")
    public ResponseEntity<CatIdResponse> update(@RequestBody CatInfoDto dto, @PathVariable long catId) {
        if (!securityChecker.isAdmin() && !securityChecker.checkContextOwnerForCat(catId)) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        if (dto.getId() == null) dto.setId(catId);
        if (!contextManager.setCurrentOwner(dto)) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        CatDto updatedCat;
        try {
            updatedCat = (CatDto) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.WEB_EXCHANGE,
                    RabbitMQConfig.ROUTING_KEY_UPDATING_OWNER, dto);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new CatIdResponse(updatedCat.getId()), HttpStatus.OK);
    }
    @DeleteMapping("/cat/{catId}")
    public void delete(@PathVariable long catId) {
        if (!securityChecker.isAdmin() && !securityChecker.checkContextOwnerForCat(catId)) throw new AccessDeniedException("Access denied");
        rabbitTemplate.convertAndSend(RabbitMQConfig.WEB_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_DELETING_CAT, catId);
    }
}

