package com.labs.lab1.entities.account;

import com.labs.lab1.valueObjects.AccountState;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Account {
    private long id;
    private long userId;
    private long bankId;
    private AccountState state;
}
