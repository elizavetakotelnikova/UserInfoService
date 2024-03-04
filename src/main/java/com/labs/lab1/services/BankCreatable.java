package com.labs.lab1.services;

import com.labs.lab1.entities.bank.Bank;
import com.labs.lab1.entities.bank.CreateBankDTO;
import exceptions.IncorrectArgumentsException;

public interface BankCreatable {
    Bank createBank(CreateBankDTO bankInfo) throws IncorrectArgumentsException;
}
