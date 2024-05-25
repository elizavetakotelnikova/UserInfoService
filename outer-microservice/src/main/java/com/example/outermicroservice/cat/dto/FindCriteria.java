package com.example.outermicroservice.cat.dto;
import com.example.jpa.Color;
import com.example.jpa.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindCriteria {
    private String name;
    private String breed;
    private Color color;
    private Long ownerId;
    private LocalDate birthday;
}
