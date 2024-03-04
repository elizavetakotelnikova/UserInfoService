package com.labs.lab1.entities.transaction;

import com.labs.lab1.valueObjects.TransactionState;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public abstract class Transaction implements Command {
    @Getter
    protected UUID id;
    @Getter
    @Setter
    protected TransactionState state = TransactionState.Commit;
}
