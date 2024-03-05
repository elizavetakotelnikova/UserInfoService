package com.labs.lab1.services.interfaces;

import com.labs.lab1.entities.transaction.Command;
import exceptions.NotEnoughMoneyException;
import exceptions.NotVerifiedException;

public interface CommandInvoker {
    void Consume(Command command) throws NotEnoughMoneyException, NotVerifiedException;
}
