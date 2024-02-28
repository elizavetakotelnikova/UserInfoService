package com.labs.lab1.entities.bank;

import com.labs.lab1.entities.customer.Customer;
import com.labs.lab1.models.SavingsAccountsConditions;
import com.labs.lab1.services.Updatable;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CreateBankDTO {
    private UUID id;
    private String name;
    private List<SavingsAccountsConditions> savingsAccountsConditions;
    private double checkingAccountPercentage;
    private double baseCommission;
    private double loanRate;
}
