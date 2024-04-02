package org.example.entities.cat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.valueObjects.Color;
import org.jetbrains.annotations.Nullable;

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
