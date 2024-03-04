package com.labs.lab1.entities.bank;

import com.labs.lab1.entities.transaction.Transaction;
import com.labs.lab1.services.BankCreatable;
import exceptions.IncorrectArgumentsException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CentralBank implements BankCreatable {
    private static CentralBank instance;
    @Getter
    public List<Bank> banks = new ArrayList<>();
    public static CentralBank getInstance() {
        if (instance == null) {
            instance = new CentralBank();
        }
        return instance;
    }

    /**
     * creating new bank
     * @param info bank info: name, conditions, limits
     * @return new bank if data is correct
     * @throws IncorrectArgumentsException if required arguments not set
     */
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

    /**
     * managing moeny transfer from one bank to another
     * @param firstBank
     * @param secondBank
     * @return commission
     */
    public double checkTransferConditions(UUID firstBank, UUID secondBank) {
        return 0.01;
        // should consist of conditions based on banks
    }

    /**
     * notifying banks to update accounts
     */
    public void NotifyBanks() {
        banks.forEach(Bank::updateAccount);
    }
}
