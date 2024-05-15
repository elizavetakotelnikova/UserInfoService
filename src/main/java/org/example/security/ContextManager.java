package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.cat.CatInfoDto;
import org.example.entities.owner.FindCriteria;
import org.example.owner.OwnerInfoDto;
import org.example.owner.OwnerService;
import org.example.user.UserService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ContextManager {
    private final UserIdentitySecurityChecker securityChecker;
    private final UserService userService;
    private final OwnerService ownerService;
    public boolean setCurrentOwner(CatInfoDto dto) {
        if (dto.getOwnerId() == null) {
            var currentUser = securityChecker.getUserId();
            var user = userService.getUserById(currentUser);
            var owner = ownerService.getOwnerByCriteria(new FindCriteria(null, user.getId()));
            if (owner.isEmpty()) return false;
            dto.setOwnerId(owner.getFirst().getId());
        }
        return true;
    }
}
