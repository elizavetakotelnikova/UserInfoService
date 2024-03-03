package com.labs.lab1.entities.account;

import com.labs.lab1.services.Updatable;
import com.labs.lab1.valueObjects.AccountState;
import exceptions.NotEnoughMoneyException;
import exceptions.NotVerifiedException;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import static java.lang.Math.abs;

@Getter
@Setter
public class CreditAccount extends Account implements Updatable {
    public CreditAccount(UUID userId, UUID bankId, double balance, double notVerifiedLimit, AccountState state, double commissionRate, double limit, double loanRate) {
        super(userId, bankId, balance, notVerifiedLimit, state);
        this.commissionRate = commissionRate;
        this.limit = limit;
        this.loanRate = loanRate;
    }
    public CreditAccount(UUID id, UUID userId, UUID bankId, double balance, double notVerifiedLimit, AccountState state, double commissionRate, double limit, double loanRate) {
        super(userId, bankId, balance, notVerifiedLimit, state);
        this.id = id;
        this.commissionRate = commissionRate;
        this.limit = limit;
        this.loanRate = loanRate;
    }

    private double commissionRate;
    private double loanRate;
    private double limit;
    @Override
    public void replenish(double amount) {
        balance += amount;
        balance -= commissionRate;
    }
    @Override
    public void withdraw(double amount) throws NotEnoughMoneyException, NotVerifiedException {
        if (state == AccountState.NotVerified && amount > notVerifiedLimit) throw new NotVerifiedException("Transaction cannot be done, account not verified");
        if (balance >= 0) {
            if (amount > (limit + balance)) throw new NotEnoughMoneyException("Transaction cannot be done");
            balance -= amount;
        }
        else {
            if (amount > (limit - abs(balance))) throw new NotEnoughMoneyException("Transaction cannot be done");
            balance -= amount;
            balance -= commissionRate;
        }
    }

    @Override
    public void makeRegularUpdate() {
        if (balance < 0) balance -= balance * (loanRate / 100);
    }
}
