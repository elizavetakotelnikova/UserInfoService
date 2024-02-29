package com.labs.lab1.entities.bank;

import com.labs.lab1.services.BankCreatable;
import exceptions.IncorrectArgumentsException;

import java.util.List;
import java.util.UUID;

public class CentralBank implements BankCreatable {
    private static CentralBank instance;
    public List<Bank> banks;
    public static CentralBank getInstance() {
        if (instance == null) {
            instance = new CentralBank();
        }
        return instance;
    }
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
    public double checkTransferConditions(UUID firstBank, UUID secondBank) {
        return 0.01;
        // should consist of conditions based on banks
    }
}
