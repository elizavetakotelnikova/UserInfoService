package org.example.security;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUser extends User {
    private final Long id;
    public CustomUser(String username, String password, boolean enabled,
                      boolean accountNonExpired, boolean credentialsNonExpired,
                      boolean accountNonLocked,
                      Collection authorities,
                      Long id) {
        super(username, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }
    public CustomUser(Long id, String username, String password, Collection authorities) {
        super(username, password, authorities);
        this.id = id;
    }
}
