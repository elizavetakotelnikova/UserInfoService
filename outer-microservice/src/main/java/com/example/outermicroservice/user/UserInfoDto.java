package com.example.outermicroservice.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.example.jpa.Role;

import java.util.List;
@Data
@AllArgsConstructor
public class UserInfoDto {
    private Long id;
    private String username;
    private String password;
    private Long ownerId;
    private List<Role> authorities;
    public void UserLoginDto(String username, String password,
                        Long ownerId, List<Role> authorities) {
        this.username = username;
        this.password = password;
        this.ownerId = ownerId;
        this.authorities = authorities;
    }
}
