package com.labs.lab1.entities.transaction;

import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.bank.CentralBank;
import com.labs.lab1.valueObjects.TransactionState;

import java.util.UUID;

public class TransferTransaction extends Transaction {
    private final Account withdrawAccount;
    private final Account replenishAccount;
    private final double backUpWithDrawAccountBalance;
    private final double backUpReplenishAccountBalance;
    private double withdrawedAmount;
    private double replenishedAmount;
    private double amount;
    public TransferTransaction(Account withdrawAccount, Account replenishAccount, double amount) {
        this.id = UUID.randomUUID();
        this.withdrawAccount = withdrawAccount;
        this.replenishAccount = replenishAccount;
        this.amount = amount;
        this.backUpWithDrawAccountBalance = withdrawAccount.getBalance();
        this.backUpReplenishAccountBalance = replenishAccount.getBalance();
    }
    @Override
    public void execute() {
        if (withdrawAccount.getBankId() != replenishAccount.getBankId()){
            var commission = CentralBank.getInstance().checkTransferConditions(withdrawAccount.getBankId(), replenishAccount.getBankId());
            amount *= commission;
        }
        try {
            withdrawAccount.withdraw(amount);
            replenishAccount.replenish(amount);
            backUp();
        }
        catch (Exception e) {
            undo();
            return;
        }
        withdrawAccount.getTransactionsHistory().add(this);
        replenishAccount.getTransactionsHistory().add(this);
        this.state = TransactionState.Commit;

    }
    public void backUp() {
        withdrawedAmount = backUpWithDrawAccountBalance - withdrawAccount.getBalance();
        replenishedAmount = backUpReplenishAccountBalance - replenishAccount.getBalance();
    }
    @Override
    public void undo() {
        if (this.state == TransactionState.Rollback) return;
        withdrawAccount.setBalance(withdrawAccount.getBalance() + withdrawedAmount);
        replenishAccount.setBalance(replenishAccount.getBalance() - replenishedAmount);
        this.state = TransactionState.Rollback;
    }
}
