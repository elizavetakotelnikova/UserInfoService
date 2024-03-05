package com.labs.lab1.entities.bank;

import com.labs.lab1.models.RangeConditionsInfo;
import com.labs.lab1.services.interfaces.BankCreatable;
import exceptions.IncorrectArgumentsException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class CentralBank implements BankCreatable {
    private static CentralBank instance;
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
    public Bank createBank(String name, List<RangeConditionsInfo> info, double checkingAccountPercentage,
                           double baseCommission, double loanRate, double notVerifiedLimit) throws IncorrectArgumentsException {
        if (name.isEmpty())
            throw new IncorrectArgumentsException("Invalid information for bank creating");
        var createdBank = new Bank(name, info,
                checkingAccountPercentage, baseCommission, loanRate,
                notVerifiedLimit);
        banks.add(createdBank);
        return createdBank;
    }

    /**
     * managing money transfer from one bank to another
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
        banks.forEach(Bank::creditPercentage);
    }
}
