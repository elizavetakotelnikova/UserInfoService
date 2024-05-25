package com.example.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatDto {
    private Long id;
    private String name;
    private String breed;
    private Color color;
    private Owner owner;
    private java.time.LocalDate birthday;
    private List<Long> friendsId;
}
