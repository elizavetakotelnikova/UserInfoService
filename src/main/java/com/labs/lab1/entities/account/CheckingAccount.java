package com.labs.lab1.entities.account;

import com.labs.lab1.services.Updatable;
import com.labs.lab1.valueObjects.AccountState;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CheckingAccount extends Account implements Updatable {
    public CheckingAccount (UUID userId, UUID bankId, double balance, double notVerifiedLimit, AccountState state, double percentage) {
        super(userId, bankId, balance, notVerifiedLimit, state);
        this.percentage = percentage;
    }
    private double percentage;

    public CheckingAccount (UUID id, UUID userId, UUID bankId, double balance, double notVerifiedLimit, AccountState state, double percentage) {
        super(userId, bankId, balance, notVerifiedLimit, state);
        this.id = id;
        this.percentage = percentage;
    }


    @Override
    public void makeRegularUpdate() {
        this.balance += this.balance * percentage;
    }
}
