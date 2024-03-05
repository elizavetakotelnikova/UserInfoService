package com.labs.lab1.services.interfaces;

import com.labs.lab1.entities.account.Account;
import exceptions.NotEnoughMoneyException;
import exceptions.NotVerifiedException;

public interface Transferable {
    void transfer(Account anotherAccount, double amount) throws NotEnoughMoneyException, NotVerifiedException;
}
