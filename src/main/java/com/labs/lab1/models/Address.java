package com.labs.lab1.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private int buildingNumber;
}
