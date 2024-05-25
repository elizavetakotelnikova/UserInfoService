package com.example.outermicroservice.security;
import com.example.jpa.*;
import com.example.outermicroservice.owner.dto.FindCriteria;
import com.example.jpa.OwnerDto;
import com.example.outermicroservice.user.services.UsersDao;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.access.AccessDeniedException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserIdentitySecurityChecker {
    private final UsersDao usersDao;
    private final RabbitTemplate rabbitTemplate;
    public static boolean checkIsTheContextUser(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("Access denied");
        }
        CustomUser details = (CustomUser) auth.getPrincipal();
        if (!Objects.equals(userId, details.getId())) {
            throw new AccessDeniedException("Access denied");
        }
        return true;
    }
    public Long getUserId() throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("Access denied");
        }
        CustomUser details = (CustomUser) auth.getPrincipal();
        return details.getId();
    }
    public boolean checkIsTheContextOwner(Long ownerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("Access denied");
        }
        CustomUser details = (CustomUser) auth.getPrincipal();
        var user = usersDao.findById(details.getId()).get();
        var owner = (OwnerDto) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.WEB_EXCHANGE, RabbitMQConfig.ROUTING_KEY_FINDING_OWNER, ownerId);
        if (owner == null) throw new AccessDeniedException("Access denied");
        if (!Objects.equals(user.getId(), owner.getUser().getId())) {
            throw new AccessDeniedException("Access denied");
        }
        return true;
    }
    public boolean checkContextOwnerForCat(Long catId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("Access denied");
        }
        CustomUser details = (CustomUser) auth.getPrincipal();

        CatDto cat = (CatDto) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.WEB_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_FINDING_CAT, catId);
        if (cat == null) throw new AccessDeniedException("Access denied");
        User user = usersDao.findById(details.getId()).orElse(null);
        var findCriteria = new FindCriteria(null, user);

        var owner = (OwnerDto) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.WEB_EXCHANGE, RabbitMQConfig.ROUTING_KEY_FINDING_OWNER_BY_CRITERIA, findCriteria);
        if (user == null || owner == null || !Objects.equals(cat.getOwner().getId(), owner.getId())) {
            throw new AccessDeniedException("Access denied");
        }
        return true;
    }
    public boolean checkContextOwnerForCat(Cat cat) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("Access denied");
        }
        CustomUser details = (CustomUser) auth.getPrincipal();
        User user = usersDao.findById(details.getId()).get();
        var findCriteria = new FindCriteria(null, user);
        var owner = (OwnerDto) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.WEB_EXCHANGE, RabbitMQConfig.ROUTING_KEY_FINDING_OWNER_BY_CRITERIA, findCriteria);
        if (owner == null || !Objects.equals(cat.getOwner().getId(), owner.getId())) {
            throw new AccessDeniedException("Access denied");
        }
        return true;
    }
    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("Access denied");
        }
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
