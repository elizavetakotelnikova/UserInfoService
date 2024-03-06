package com.labs.lab1.entities.transaction;

import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.bank.Bank;
import com.labs.lab1.entities.transaction.Command;
import com.labs.lab1.valueObjects.TransactionState;
import lombok.Getter;

import java.util.UUID;

public class ReplenishTransaction extends Transaction {
    private final Account account;
    private final double backUpAccountBalance;
    private double replenishedAmount;
    private final double amount;
    public ReplenishTransaction(Account account, double amount) {
        this.id = UUID.randomUUID();
        this.account = account;
        this.amount = amount;
        this.backUpAccountBalance = account.getBalance();
    }

    /**
     * replenish transaction execution
     * @param bank bank which provides the transaction
     */
    @Override
    public void execute(Bank bank) {
        try {
            account.replenish(amount);
            backUp();
        }
        catch (Exception e) {
            undo();
            return;
        }
        account.getTransactionsHistory().add(this);
        this.state = TransactionState.Commit;
    }
    public void backUp() {
        replenishedAmount = account.getBalance() - backUpAccountBalance;
    }
    /**
     * rollback of transaction
     */
    @Override
    public void undo() {
        if (this.state == TransactionState.Rollback) return;
        account.setBalance(account.getBalance() - replenishedAmount);
        this.state = TransactionState.Rollback;
    }
}
