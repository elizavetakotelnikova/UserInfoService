package com.labs.lab1.entities.account;

import com.labs.lab1.services.Updatable;
import com.labs.lab1.valueObjects.AccountState;
import exceptions.NotEnoughMoneyException;
import exceptions.NotVerifiedException;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import static java.lang.Math.abs;

@Getter
@Setter
public class CreditAccount extends Account implements Updatable {
    /**
     *
     * @param userId - user id
     * @param bankId - bank id
     * @param balance - current account balance
     * @param notVerifiedLimit - withdraw/replenish limit for not verified accounts
     * @param state - account state (verified/not verified)
     * @param commissionRate - withdraw/replenish commission when balance < 0 (in rubles)
     * @param limit - credit limit
     * @param loanRate - credit percentage
     */
    public CreditAccount(UUID userId, UUID bankId, double balance, double notVerifiedLimit, AccountState state, double commissionRate, double limit, double loanRate) {
        super(userId, bankId, balance, notVerifiedLimit, state);
        this.commissionRate = commissionRate;
        this.limit = limit;
        this.loanRate = loanRate;
    }
    public CreditAccount(UUID id, UUID userId, UUID bankId, double balance, double notVerifiedLimit, AccountState state, double commissionRate, double limit, double loanRate) {
        super(userId, bankId, balance, notVerifiedLimit, state);
        this.id = id;
        this.commissionRate = commissionRate;
        this.limit = limit;
        this.loanRate = loanRate;
    }

    private double commissionRate;
    private double loanRate;
    private double limit;

    /**
     * replenishing the account (including commission rate)
     * @param amount - amount of money
     */
    @Override
    public void replenish(double amount) {
        balance += amount;
        balance -= commissionRate;
    }

    /**
     * withdrawingmoney
     * @param amount - amount of money
     * @throws NotEnoughMoneyException - not enough money to withdraw or credit limit is fully used
     * @throws NotVerifiedException - trying to withdraw amount more than not verified limit
     */
    @Override
    public void withdraw(double amount) throws NotEnoughMoneyException, NotVerifiedException {
        if (state == AccountState.NotVerified && amount > notVerifiedLimit) throw new NotVerifiedException("Transaction cannot be done, account not verified");
        if (balance >= 0) {
            if (amount > (limit + balance)) throw new NotEnoughMoneyException("Transaction cannot be done");
            balance -= amount;
        }
        else {
            if (amount > (limit - abs(balance))) throw new NotEnoughMoneyException("Transaction cannot be done");
            balance -= amount;
            balance -= commissionRate;
        }
    }

    /**
     * monthly update => loanRate is charged
     */
    @Override
    public void makeRegularUpdate() {
        if (balance < 0) balance -= balance * (loanRate / 100);
    }
}
