package com.labs.lab1.entities.transaction;

public interface Command {
    void execute();
    void undo();
}
