package com.labs.lab1.models;

import lombok.Data;

@Data
public class Address {
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private String buildingNumber;
}
