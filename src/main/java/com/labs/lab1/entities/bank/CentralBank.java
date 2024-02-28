package com.labs.lab1.entities.bank;

import com.labs.lab1.services.BankCreatable;
import com.labs.lab1.services.PercentageCreditable;
import com.labs.lab1.services.Updatable;
import exceptions.IncorrectArgumentsException;

import java.util.List;

public class CentralBank implements BankCreatable {
    public List<PercentageCreditable> banks;
    @Override
    public Bank createBank(CreateBankDTO info) throws IncorrectArgumentsException {
        if (info.getName().isEmpty())
            throw new IncorrectArgumentsException("Invalid information for bank creating");
        var createdBank = new Bank(info.getName(), info.getSavingsAccountsConditions(),
                info.getCheckingAccountPercentage(), info.getBaseCommission(), info.getLoanRate(),
                info.getNotVerifiedLimit());
        banks.add(createdBank);
        return createdBank;

    }
}
