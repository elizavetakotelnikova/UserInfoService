package org.example.owner;
import lombok.*;
import org.example.entities.cat.Cat;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerInfoDto {
    private Long id;
    private java.time.LocalDate birthday;
    private List<Cat> cats;
}

