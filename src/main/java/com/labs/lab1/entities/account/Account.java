package com.labs.lab1.entities.account;

import com.labs.lab1.services.Replenishable;
import com.labs.lab1.services.Transferable;
import com.labs.lab1.services.Withdrowable;
import com.labs.lab1.valueObjects.AccountState;
import exceptions.NotEnoughMoneyException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public abstract class Account implements Replenishable, Withdrowable, Transferable {
    protected UUID id;
    protected UUID userId;
    protected UUID bankId;
    protected double balance;
    protected AccountState state;

    @Override
    public void replenish(double amount) {
        balance += amount;
    }

    @Override
    public void transfer(Account anotherAccount, double amount) throws NotEnoughMoneyException {
        if (balance < amount) throw new NotEnoughMoneyException("Transaction cannot be done");
        balance -= amount;
        anotherAccount.balance += amount;
    }

    @Override
    public void withdraw(double amount) throws NotEnoughMoneyException {
        if (balance < amount) throw new NotEnoughMoneyException("Transaction cannot be done");
        balance -= amount;
    }
}
