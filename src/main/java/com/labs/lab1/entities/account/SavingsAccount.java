package com.labs.lab1.entities.account;

import com.labs.lab1.services.Updatable;
import com.labs.lab1.valueObjects.AccountState;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class SavingsAccount extends Account implements Updatable {
    public SavingsAccount(UUID id, UUID userId, UUID bankId, double balance, AccountState state,
                          LocalDate endDate, double percentage) {
        super(id, userId, bankId, balance, state);
        this.endDate = endDate;
        this.percentage = percentage;
    }
    private LocalDate endDate;
    private double percentage;

    @Override
    public void makeRegularUpdate() {
        if (endDate.isAfter(LocalDate.now())) balance += balance* percentage;
    }
}
