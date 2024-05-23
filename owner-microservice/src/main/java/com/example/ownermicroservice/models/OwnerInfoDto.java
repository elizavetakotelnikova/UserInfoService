package com.example.ownermicroservice.models;

import com.example.jpa.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.jpa.Cat;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerInfoDto {
    private Long id;
    private java.time.LocalDate birthday;
    private List<Cat> cats;
    private User user;
}

