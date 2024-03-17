package org.example.entities.owner;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
public class FindCriteria {
    @Nullable
    private java.time.LocalDate birthday;
}
