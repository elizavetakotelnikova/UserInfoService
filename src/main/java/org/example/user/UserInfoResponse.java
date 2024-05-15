package org.example.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.entities.user.Role;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String username;
    private List<Role> authorities;
}
