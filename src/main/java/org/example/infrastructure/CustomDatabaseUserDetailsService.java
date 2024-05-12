package org.example.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.entities.user.UsersDao;
import org.example.entities.user.UsersDao;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomDatabaseUserDetailsService implements UserDetailsService {
    private final UsersDao usersDao;
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = usersDao.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("No such user exists");
        List<SimpleGrantedAuthority> grantedAuthorities = user.getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.toString())).collect(Collectors.toList());
        return new CustomUser(user.getId(), user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
