package com.example.catmicroservice.valueObjects;

import com.example.jpa.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindCriteria {
    private String name;
    private String breed;
    private Color color;
    private Long ownerId;
    @Nullable
    private java.time.LocalDate birthday;
}
