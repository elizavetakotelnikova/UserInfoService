package com.labs.lab1.services;

import exceptions.NegativeBalanceException;

public interface Withdrowable {
    public void withdraw(double amount) throws NegativeBalanceException;
}
