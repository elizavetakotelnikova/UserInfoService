package org.example.entities.cat;

import lombok.Data;
import org.example.valueObjects.Color;
import org.jetbrains.annotations.Nullable;

@Data
public class FindCriteria {
    private String name;
    private String breed;
    private Color color;
    private long ownerId;
    @Nullable
    private java.time.LocalDate birthday;
}
