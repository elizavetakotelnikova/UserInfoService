package com.labs.lab1.entities.user;

import com.labs.lab1.models.Address;
import com.labs.lab1.models.PassportData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class User {
    private long id;
    private String firstName;
    private String lastName;
    private Address address;
    private PassportData passportData;
}
