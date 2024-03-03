package com.labs.lab1.entities.transaction;

import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.transaction.Command;
import com.labs.lab1.valueObjects.TransactionState;
import exceptions.NotEnoughMoneyException;
import exceptions.NotVerifiedException;
import lombok.Getter;

import java.util.UUID;

public class WithdrawTransaction extends Transaction {
    @Getter
    private final Account account;
    private final double backUpAccountBalance;
    private double withdrawedAmount;
    @Getter
    private final double amount;
    public WithdrawTransaction(Account account, double amount) {
        this.id = UUID.randomUUID();
        this.account = account;
        this.amount = amount;
        this.backUpAccountBalance = account.getBalance();
    }
    @Override
    public void execute() {
        try {
            account.withdraw(amount);
            backUp();
        }
        catch (Exception e) {
            undo();
            return;
        }
        account.getTransactionsHistory().add(this);
        state = TransactionState.Commit;
    }
    public void backUp() {
        withdrawedAmount = backUpAccountBalance - account.getBalance();
    }
    @Override
    public void undo() {
        if (state == TransactionState.Rollback) return;
        account.setBalance(account.getBalance() + withdrawedAmount);
        state = TransactionState.Rollback;
    }
}
