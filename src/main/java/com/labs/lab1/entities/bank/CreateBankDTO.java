package com.labs.lab1.entities.bank;
import com.labs.lab1.models.SavingsAccountsConditions;
import lombok.Data;
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
    private double notVerifiedLimit;
}
