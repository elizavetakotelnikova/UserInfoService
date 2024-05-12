package org.example.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.example.entities.owner.OwnersDao;
import org.example.entities.user.User;
import org.example.entities.user.UsersDao;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserIdentitySecurityChecker {
    private final CatsDao catsDao;
    private final UsersDao usersDao;
    public static boolean checkUserIdentity(Long userId) {
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
    public boolean checkOwnerIdentity(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("Access denied");
        }
        CustomUser details = (CustomUser) auth.getPrincipal();
        Long ownerId = usersDao.findById(userId).get().getOwner().getId();
        if (!Objects.equals(userId, details.getId()) || ownerId == null) {
            throw new AccessDeniedException("Access denied");
        }
        return true;
    }
    public boolean checkUserIdentityForCats(Long catId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("Access denied");
        }
        CustomUser details = (CustomUser) auth.getPrincipal();
        Long ownerId = catsDao.findById(catId).get().getId();
        if (!Objects.equals(ownerId, details.getId())) {
            throw new AccessDeniedException("Access denied");
        }
        return true;
    }
    public boolean checkUserIdentityForCats(Cat cat) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("Access denied");
        }
        CustomUser details = (CustomUser) auth.getPrincipal();
        User user = usersDao.findById(details.getId()).get();
        if (!Objects.equals(cat.getOwner().getId(), user.getOwner().getId())) {
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
