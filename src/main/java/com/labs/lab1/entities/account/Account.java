package com.labs.lab1.entities.account;

import com.labs.lab1.services.Replenishable;
import com.labs.lab1.services.Transferable;
import com.labs.lab1.services.Withdrowable;
import com.labs.lab1.valueObjects.AccountState;
import exceptions.NotEnoughMoneyException;
import exceptions.NotVerifiedException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public abstract class Account implements Replenishable, Withdrowable, Transferable {
    protected UUID id;
    protected UUID userId;
    protected UUID bankId;
    protected double balance;
    protected double notVerifiedLimit;
    protected AccountState state;
    public Account(UUID userId, UUID bankId, double balance, double notVerifiedLimit, AccountState state) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.bankId = bankId;
        this.balance = balance;
        this.notVerifiedLimit = notVerifiedLimit;
        this.state = state;
    }

    @Override
    public void replenish(double amount) {
        balance += amount;
    }

    @Override
    public void transfer(Account anotherAccount, double amount) throws NotEnoughMoneyException, NotVerifiedException {
        if (balance < amount) throw new NotEnoughMoneyException("Transaction cannot be done");
        if (state == AccountState.NotVerified && amount > notVerifiedLimit) throw new NotVerifiedException("Transaction cannot be done, account not verified");
        balance -= amount;
        anotherAccount.balance += amount;
    }

    @Override
    public void withdraw(double amount) throws NotEnoughMoneyException, NotVerifiedException {
        if (balance < amount) throw new NotEnoughMoneyException("Transaction cannot be done");
        if (state == AccountState.NotVerified && amount > notVerifiedLimit) throw new NotVerifiedException("Transaction cannot be done, account not verified");
        balance -= amount;
    }
}
