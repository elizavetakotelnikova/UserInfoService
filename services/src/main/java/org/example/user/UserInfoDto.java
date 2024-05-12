package org.example.user;

import lombok.Data;
import org.example.entities.user.Role;

import java.util.List;

@Data
public class UserInfoDto {
    private Long id;
    private Long ownerId;
    List<Role> authorities;
    private String username;
    private String password;
}
