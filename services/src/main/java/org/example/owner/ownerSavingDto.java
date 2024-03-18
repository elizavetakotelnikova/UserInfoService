package org.example.owner;
import lombok.*;
import java.util.List;

@Data
public class ownerSavingDto {
    private Long id;
    private java.time.LocalDate birthday;
    private List<Long> cats;
}

