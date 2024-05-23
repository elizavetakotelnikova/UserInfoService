package com.example.outermicroservice.cat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.jpa.Color;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatSavingRequest {
    private String name;
    private String breed;
    private Color color;
    private Long ownerId;
    private java.time.LocalDate birthday;
    private List<Long> friendsId;
}

