package com.labs.lab1.services;

import exceptions.NotEnoughMoneyException;

public interface Withdrowable {
    public void withdraw(double amount) throws NotEnoughMoneyException;
}
