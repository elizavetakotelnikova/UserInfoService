package com.labs.lab1.entities.transaction;

import exceptions.NotEnoughMoneyException;
import exceptions.NotVerifiedException;

public interface Command {
    void execute();
    void undo();
}
