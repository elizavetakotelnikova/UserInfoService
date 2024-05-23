package com.example.outermicroservice.user;

import com.example.outermicroservice.role.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.example.jpa.Role;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String username;
    private List<Role> authorities;
}
