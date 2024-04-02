package org.example.owner.responseModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerCreateResponse {
    private Long id;
    private java.time.LocalDate birthday;
    private List<Long> cats;
}
