package org.example.entities.owner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindCriteria {
    @Nullable
    private java.time.LocalDate birthday;
    private Long userId;
}
