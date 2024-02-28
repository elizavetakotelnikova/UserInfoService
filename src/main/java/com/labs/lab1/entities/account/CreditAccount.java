package com.labs.lab1.entities.account;

import com.labs.lab1.services.Updatable;
import com.labs.lab1.valueObjects.AccountState;
import exceptions.FailedTransactionException;
import exceptions.NotEnoughMoneyException;
import lombok.Getter;

import java.util.UUID;

import static java.lang.Math.abs;

@Getter
public class CreditAccount extends Account implements Updatable {
    public CreditAccount(UUID id, UUID userId, UUID bankId, double balance, AccountState state, double commissionRate, double limit, double loanRate) {
        super(id, userId, bankId, balance, state);
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
    public void withdraw(double amount) throws NotEnoughMoneyException {
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
    public void transfer(Account anotherAccount, double amount) throws NotEnoughMoneyException {
        withdraw(amount);
        replenish(amount);
    }

    @Override
    public void makeRegularUpdate() {
        if (balance < 0) balance -= balance * loanRate;
    }
}
