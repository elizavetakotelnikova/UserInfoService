package com.labs.lab1.services;

import com.labs.lab1.entities.bank.Bank;
import com.labs.lab1.entities.bank.BankInfo;

public interface BankCreatable {
    public Bank createBank(BankInfo bankInfo);
}
