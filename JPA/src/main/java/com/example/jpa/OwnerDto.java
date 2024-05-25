package com.example.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDto {
    private Long id;
    private java.time.LocalDate birthday;
    private List<Cat> cats;
    private User user;
}

