package com.labs.lab1.services;

import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.customer.Customer;
import com.labs.lab1.models.CreateAccountDTO;
import com.labs.lab1.valueObjects.AccountType;

public interface AccountCreatable {
    Account createAccount(Customer customer, CreateAccountDTO info);
}
