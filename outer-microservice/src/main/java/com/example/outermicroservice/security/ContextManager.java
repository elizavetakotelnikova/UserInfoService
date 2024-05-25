package com.example.outermicroservice.security;

import com.example.outermicroservice.rabbitMQ.RabbitMQConfig;
import com.example.outermicroservice.owner.dto.FindCriteria;
import com.example.jpa.OwnerDto;
import com.example.outermicroservice.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import com.example.outermicroservice.cat.dto.CatInfoDto;

import java.util.List;


@RequiredArgsConstructor
@Component
public class ContextManager {
    private final RabbitTemplate rabbitTemplate;
    private final UserIdentitySecurityChecker securityChecker;
    private final UserService userService;
    public boolean setCurrentOwner(CatInfoDto dto) {
        if (dto.getOwnerId() == null) {
            var currentUser = securityChecker.getUserId();
            var user = userService.getUserById(currentUser);
            var findCriteria = new FindCriteria(null, user);
            var owner = (List<OwnerDto>) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.WEB_EXCHANGE, RabbitMQConfig.ROUTING_KEY_FINDING_OWNER_BY_CRITERIA, findCriteria);
            if (owner == null || owner.isEmpty() || owner.getFirst() == null) return false;
            dto.setOwnerId(owner.getFirst().getId());
        }
        return true;
    }
}
