package org.example.cat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.valueObjects.Color;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class catSavingDto {
    private String name;
    private String breed;
    private Color color;
    private Long ownerId;
    private java.time.LocalDate birthday;
    private List<Long> friendsId;
}