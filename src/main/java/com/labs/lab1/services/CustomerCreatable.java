package com.labs.lab1.services;

import com.labs.lab1.entities.customer.Customer;
import com.labs.lab1.models.Address;
import com.labs.lab1.models.PassportData;
import exceptions.IncorrectArgumentsException;

public interface CustomerCreatable {
    Customer createCustomer(Address address, PassportData passportData, String  firstName, String lastName) throws IncorrectArgumentsException;
}
