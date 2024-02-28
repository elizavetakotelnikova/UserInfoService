package com.labs.lab1.entities.account;

import com.labs.lab1.services.Updatable;
import com.labs.lab1.valueObjects.AccountState;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreditAccount extends Account implements Updatable {
    public CreditAccount(UUID id, UUID userId, UUID bankId, double balance, AccountState state, double commissionRate, double limit) {
        super(id, userId, bankId, balance, state);
        this.commissionRate = commissionRate;
        this.limit = limit;
    }
    private double commissionRate;
    private double limit;

    @Override
    public void makeRegularUpdate() {
        if (balance < 0) balance -= balance * commissionRate;
    }
}
