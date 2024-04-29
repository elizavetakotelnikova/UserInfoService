package org.example.owner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnersSavingDto {
    private java.time.LocalDate birthday;
    private List<Long> cats;
}
