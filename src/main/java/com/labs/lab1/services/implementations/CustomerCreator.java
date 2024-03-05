package com.labs.lab1.services.implementations;

import com.labs.lab1.entities.bank.Bank;
import com.labs.lab1.entities.customer.Customer;
import com.labs.lab1.models.Address;
import com.labs.lab1.models.PassportData;
import com.labs.lab1.services.interfaces.CustomerCreatable;
import exceptions.IncorrectArgumentsException;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class CustomerCreator implements CustomerCreatable {
    private Bank bank;
    /**
     * creating customer and adding to customers list
     * @param address
     * @param passportData
     * @param firstName
     * @param lastName
     * @return new customer
     * @throws IncorrectArgumentsException when first name or last name is not set
     */
    @Override
    public Customer createCustomer(Address address, PassportData passportData, String firstName, String lastName) throws IncorrectArgumentsException {
        if (firstName.isEmpty() || lastName.isEmpty()) throw new IncorrectArgumentsException("Required fields are not set");
        var createdCustomer = new Customer(firstName, lastName, address, passportData);
        bank.getCustomers().add(createdCustomer);
        return createdCustomer;
    }
}
