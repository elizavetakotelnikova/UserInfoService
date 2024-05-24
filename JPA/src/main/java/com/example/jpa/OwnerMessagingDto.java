package com.example.jpa;

import com.example.jpa.Cat;
import com.example.jpa.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerMessagingDto {
    private Long id;
    private java.time.LocalDate birthday;
    private List<Cat> cats;
    private User user;
}

