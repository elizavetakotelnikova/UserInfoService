package com.labs.lab1.services;

import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.customer.Customer;
import com.labs.lab1.entities.account.CreateAccountDTO;
import exceptions.IncorrectArgumentsException;

public interface AccountCreatable {
    Account createAccount(Customer customer, CreateAccountDTO info) throws IncorrectArgumentsException;
}
