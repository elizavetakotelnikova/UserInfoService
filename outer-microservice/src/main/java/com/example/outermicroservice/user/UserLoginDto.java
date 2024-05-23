package com.example.outermicroservice.user;

import lombok.Data;

@Data
public class UserLoginDto {
    private String username;
    private String password;
}
