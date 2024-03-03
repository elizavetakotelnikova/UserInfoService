package com.labs.lab1.entities.account;

import com.labs.lab1.services.Updatable;
import com.labs.lab1.valueObjects.AccountState;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CheckingAccount extends Account implements Updatable {
    /**
     * Checking account
     * @param userId - user id
     * @param bankId - bank id
     * @param balance - current account balance
     * @param notVerifiedLimit - withdraw/replenish limit for not verified accounts
     * @param state - account state (verified/not verified)
     * @param percentage - balance percentage
     */
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

    /**
     * monthly account update (percentage calculating)
     */
    @Override
    public void makeRegularUpdate() {
        this.balance += this.balance * (percentage / 100);
    }
}
