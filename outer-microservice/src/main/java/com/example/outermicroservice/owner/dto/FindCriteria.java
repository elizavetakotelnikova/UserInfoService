package com.example.outermicroservice.owner.dto;
import com.example.jpa.User;
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
    private User user;
}
