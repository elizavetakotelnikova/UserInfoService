package com.labs.lab1.models;

import com.labs.lab1.valueObjects.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Transaction {
    private UUID id;
    private TransactionType transactionType;
}
