package com.labs.lab1.entities.bank;

import com.labs.lab1.services.BankCreatable;
import com.labs.lab1.services.PercentageCreditable;
import exceptions.IncorrectArgumentsException;

import java.util.List;

public class CentralBank implements BankCreatable {
    public List<PercentageCreditable> banksWithSavingsAccounts;
    public Bank CreateBank(BankInfo info) throws IncorrectArgumentsException {
        if (info.getName().isEmpty())
            throw new IncorrectArgumentsException("Invalid information for bank creating");
        var createdBank = new Bank(info.getName());
        banksWithSavingsAccounts.add(createdBank);
        return createdBank;

    }
}
