package com.labs.lab1.entities.account;

import com.labs.lab1.valueObjects.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAccountDTO {
    double amount; //nullable?
    Integer monthsQuantity;
    AccountType type;
    Double limit;
}
