package com.labs.lab1.entities.account;

import com.labs.lab1.services.Updatable;
import com.labs.lab1.valueObjects.AccountState;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CheckingAccount extends Account implements Updatable {
    public CheckingAccount(UUID id, UUID userId, UUID bankId, double balance, AccountState state, double percentage) {
        super(id, userId, bankId, balance, state);
        this.percentage = percentage;
    }
    private double percentage;

    @Override
    public void makeRegularUpdate() {
        this.balance += this.balance * percentage;
    }
}
