package com.example.outermicroservice.cat.dto;

import com.example.jpa.Color;
import com.example.jpa.Owner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatInfoDto {
    private Long id;
    private String name;
    private String breed;
    private Color color;
    private Long ownerId;
    private java.time.LocalDate birthday;
    private List<Long> friendsId;
}
