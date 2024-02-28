package com.labs.lab1.models;

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
