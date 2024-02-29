package com.labs.lab1.services;

import com.labs.lab1.entities.transaction.Command;

public interface CommandInvoker {
    void Consume(Command command);
}
