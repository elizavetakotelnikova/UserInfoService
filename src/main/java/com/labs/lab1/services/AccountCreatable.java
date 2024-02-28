package com.labs.lab1.services;

import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.customer.Customer;
import com.labs.lab1.entities.account.CreateAccountDTO;

public interface AccountCreatable {
    Account createAccount(Customer customer, CreateAccountDTO info);
}
