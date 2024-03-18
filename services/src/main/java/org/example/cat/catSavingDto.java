package org.example.cat;

import lombok.Data;
import org.example.Color;

import java.util.List;

@Data
public class catSavingDto {
    private String name;
    private String breed;
    private Color color;
    private Long ownerId;
    private java.time.LocalDate birthday;
    private List<Long> friendsId;
}
