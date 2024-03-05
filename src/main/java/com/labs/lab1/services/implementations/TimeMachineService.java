package com.labs.lab1.services.implementations;

import com.labs.lab1.entities.account.Account;
import com.labs.lab1.services.interfaces.Updatable;

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
