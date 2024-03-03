package com.labs.lab1.entities.transaction;

import com.labs.lab1.entities.bank.Bank;
import exceptions.NotEnoughMoneyException;
import exceptions.NotVerifiedException;

public interface Command {
    void execute(Bank bank);
    void undo();
}
