package com.labs.lab1.services;

import exceptions.NotEnoughMoneyException;
import exceptions.NotVerifiedException;

public interface Withdrowable {
    void withdraw(double amount) throws NotEnoughMoneyException, NotVerifiedException;
}
