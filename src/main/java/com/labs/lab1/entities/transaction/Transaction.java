package com.labs.lab1.entities.transaction;

import com.labs.lab1.valueObjects.TransactionState;
import lombok.Getter;

import java.util.UUID;

public abstract class Transaction implements Command {
    @Getter
    protected UUID id;
    protected TransactionState state = TransactionState.Commit;
}
