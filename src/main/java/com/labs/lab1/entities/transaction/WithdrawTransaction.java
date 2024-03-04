package com.labs.lab1.entities.transaction;

import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.bank.Bank;
import com.labs.lab1.valueObjects.AccountState;
import com.labs.lab1.valueObjects.TransactionState;
import exceptions.NotVerifiedException;
import lombok.Getter;

import java.util.UUID;

public class WithdrawTransaction extends Transaction {
    @Getter
    private final Account account;
    private final double backUpAccountBalance;
    private double withdrewAmount;
    @Getter
    private final double amount;
    public WithdrawTransaction(Account account, double amount) {
        this.id = UUID.randomUUID();
        this.account = account;
        this.amount = amount;
        this.backUpAccountBalance = account.getBalance();
    }
    @Override
    public void execute(Bank bank) {
        try {
            if ((account.getState() == AccountState.NotVerified) && (amount > bank.getNotVerifiedLimit()))
                throw new NotVerifiedException("Transaction cannot be done, account not verified");
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
        withdrewAmount = backUpAccountBalance - account.getBalance();
    }
    @Override
    public void undo() {
        if (state == TransactionState.Rollback) return;
        account.setBalance(account.getBalance() + withdrewAmount);
        state = TransactionState.Rollback;
    }
}
