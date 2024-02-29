package com.labs.lab1.entities.bank;
import com.labs.lab1.models.RangeConditionsInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class CreateBankDTO {
    private String name;
    private List<RangeConditionsInfo> savingsAccountsConditions;
    private double checkingAccountPercentage;
    private double baseCommission;
    private double loanRate;
    private double notVerifiedLimit;
}
