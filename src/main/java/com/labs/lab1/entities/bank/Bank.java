package com.labs.lab1.entities.bank;

import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.user.User;
import com.labs.lab1.services.CreateAccount;
import com.labs.lab1.services.CreateUser;

public class Bank implements CreateUser, CreateAccount {
    private long id;
    private String name;
    @Override
    public User CreateUser(User user) {

    }
    @Override
    public Account CreateAccount(Account account) {

    }
}
