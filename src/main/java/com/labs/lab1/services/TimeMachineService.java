package com.labs.lab1.services;

import com.labs.lab1.entities.account.Account;

public class TimeMachineService {
    public void makeAllUpdates(Updatable updatable, int monthsQuantity) {
        for (int i = 0; i < monthsQuantity; i++) {
            updatable.makeRegularUpdate();
        }
    }
    public Account speedUpTime(Account account, int monthsQuantity) {
        if (account instanceof Updatable) {
            makeAllUpdates((Updatable) account, monthsQuantity);
        }
        return account;
    }
}
