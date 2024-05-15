package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.entities.cat.Cat;
import org.example.entities.cat.CatsDao;
import org.example.entities.owner.Owner;
import org.example.entities.user.User;
import org.example.entities.user.UsersDao;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserIdentitySecurityChecker {
    private final CatsDao catsDao;
    private final UsersDao usersDao;
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
    public Long getUserId() {
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
        User user = usersDao.findById(details.getId()).orElse(null);
        if (user == null) throw new AccessDeniedException("Access denied");
        Owner owner = user.getOwner();
        if (owner == null || !Objects.equals(ownerId, owner.getId())) {
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
        Cat cat = catsDao.findById(catId).orElse(null);
        if (cat == null) throw new AccessDeniedException("Access denied");
        User user = usersDao.findById(details.getId()).orElse(null);
        if (user == null || !Objects.equals(cat.getOwner().getId(), user.getOwner().getId())) {
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
