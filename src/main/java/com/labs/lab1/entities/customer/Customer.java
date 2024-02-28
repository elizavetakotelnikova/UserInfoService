package com.labs.lab1.entities.customer;

import com.labs.lab1.models.Address;
import com.labs.lab1.models.PassportData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Customer {
    private UUID id;
    private String firstName;
    private String lastName;
    private Address address;
    private PassportData passportData;
    public Customer(String firstName, String lastName, Address address, PassportData passportData) {
        id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.passportData = passportData;
    }
}
