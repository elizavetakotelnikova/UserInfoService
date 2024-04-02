package org.example.cat.responseModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entities.cat.Cat;
import org.example.entities.owner.Owner;
import org.example.valueObjects.Color;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatUpdateResponse {
    private Long id;
    private String name;
    private String breed;
    private Color color;
    private Owner owner;
    private java.time.LocalDate birthday;
    private List<Cat> friends;
}
