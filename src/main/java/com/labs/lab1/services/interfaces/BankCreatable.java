package com.labs.lab1.services.interfaces;

import com.labs.lab1.entities.bank.Bank;
import com.labs.lab1.models.RangeConditionsInfo;
import exceptions.IncorrectArgumentsException;

import java.util.List;

public interface BankCreatable {
    Bank createBank(String name, List<RangeConditionsInfo> info, double checkingAccountPercentage,
                    double baseCommission, double loanRate, double notVerifiedLimit) throws IncorrectArgumentsException;
}
