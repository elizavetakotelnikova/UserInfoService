package com.labs.lab1.services;

import com.labs.lab1.entities.account.Account;
import exceptions.NotEnoughMoneyException;

public interface Transferable {
    public void transfer(Account anotherAccount, double amount) throws NotEnoughMoneyException;
}
