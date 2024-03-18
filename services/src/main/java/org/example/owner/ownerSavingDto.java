package org.example.owner;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ownerSavingDto {
    private java.time.LocalDate birthday;
    private List<Long> cats;
}

