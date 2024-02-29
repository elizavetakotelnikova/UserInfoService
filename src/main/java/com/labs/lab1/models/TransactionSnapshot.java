package com.labs.lab1.models;

import com.labs.lab1.entities.account.Account;
import lombok.Data;

import java.util.UUID;

@Data
public class TransactionSnapshot {
    private Transaction transaction; //или айди
    private Account firstAccount;
    private Account secondAccount;
}
