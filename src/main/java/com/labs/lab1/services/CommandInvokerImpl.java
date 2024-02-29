package com.labs.lab1.services;

import com.labs.lab1.entities.transaction.Command;

public class CommandInvokerImpl implements CommandInvoker {
    public void Consume(Command command) {
        command.execute();
    }
}
