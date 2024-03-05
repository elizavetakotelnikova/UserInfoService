package com.labs.lab1.entities.bank;

import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.account.CheckingAccount;
import com.labs.lab1.entities.account.CreditAccount;
import com.labs.lab1.entities.account.SavingsAccount;
import com.labs.lab1.entities.customer.Customer;
import com.labs.lab1.entities.transaction.Transaction;
import com.labs.lab1.models.*;
import com.labs.lab1.services.interfaces.NotificationGetable;
import com.labs.lab1.services.interfaces.Observable;
import com.labs.lab1.services.interfaces.PercentageCreditable;
import com.labs.lab1.services.interfaces.Updatable;
import exceptions.NotVerifiedException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Bank implements PercentageCreditable, Observable {
    private UUID id;
    @Setter
    private String name;
    private List<Customer> customers = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private List<RangeConditionsInfo> savingsAccountsConditions;
    private List<Transaction> transactions = new ArrayList<>();
    private List<NotificationGetable> subscribers = new ArrayList<>();
    private double checkingAccountPercentage;
    private double baseCreditCommission;
    private double loanRate;
    private double notVerifiedLimit;
    /**
     * bank constructor
     * @param name - bank name
     * @param savingsAccountsConditions - conditions for savings account
     * @param checkingAccountPercentage - balance percentage for checking account
     * @param baseCreditCommission - commission for credit accounts
     * @param loanRate - rate for credit accounts
     * @param notVerifiedLimit - withdraw/replenish limit for not verified accounts
     */
    public Bank(String name, List<RangeConditionsInfo> savingsAccountsConditions,
                double checkingAccountPercentage, double baseCreditCommission, double loanRate, double notVerifiedLimit) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.savingsAccountsConditions = savingsAccountsConditions;
        this.checkingAccountPercentage = checkingAccountPercentage;
        this.baseCreditCommission = baseCreditCommission;
        this.loanRate = loanRate;
        this.notVerifiedLimit = notVerifiedLimit;
    }
    /**
     * subscribing to bank notification
     * @param subscriber notification message
     */
    @Override
    public void RegisterObserver(NotificationGetable subscriber) {
        subscribers.add(subscriber);
    }
    /**
     * removing notifications' subscriber
     * @param subscriber notification message
     */
    @Override
    public void RemoveObserver(NotificationGetable subscriber) {
        subscribers.remove(subscriber);
    }
    /**
     * notifying all subscribers
     * @param message notification message
     */
    @Override
    public void NotifyObservers(String message) {
        subscribers.forEach(x -> x.getNotification(message));
    }
    /**
     * update every account in a bank
     */
    @Override
    public void creditPercentage() {
        accounts.forEach(Updatable::makeRegularUpdate);
    }
    /**
     * changing commission for credit accounts and notifying subscribed users
     * @param updatedCommission new commission
     */
    public void changeBaseCommission(double updatedCommission) {
        baseCreditCommission = updatedCommission;
        var accountsList = accounts.stream().filter(CreditAccount.class::isInstance).toList();
        for (Account account : accountsList) {
            ((CreditAccount) (account)).setCommissionRate(baseCreditCommission);
            var customer = customers.stream().filter(x -> x.getId().equals(account.getUserId())).findAny().get();
            if (subscribers.contains(customer)) customer.getNotification("New commission set by bank");
        }
    }
    /**
     * changing conditions for saving account and notifying subscribed users
     * @param updatedSavingsAccountConditions new conditions for savings account
     */
    public void changeSavingsPercentageCommission(List<RangeConditionsInfo> updatedSavingsAccountConditions) {
        savingsAccountsConditions = updatedSavingsAccountConditions;
        var accountsList = accounts.stream().filter(SavingsAccount.class::isInstance).toList();
        for (Account account : accountsList) {
            //((SavingsAccount) (account)).setPercentage(findConditions(account.getBalance()).getPercentage());
            var customer = customers.stream().filter(x -> x.getId().equals(account.getUserId())).findAny().get();
            if (subscribers.contains(customer)) customer.getNotification("New conditions offered by bank, see updated conditions");
        }
    }
    /**
     * changing checking accounts percentage and notifying subscribed users
     * @param updatedCheckingAccountPercentage new checking account percentage
     */
    public void changeCheckingAccountsPercentage(double updatedCheckingAccountPercentage) {
        checkingAccountPercentage = updatedCheckingAccountPercentage;
        var accountsList = accounts.stream().filter(CheckingAccount.class::isInstance).toList();
        for (Account account : accountsList) {
            ((CheckingAccount) (account)).setPercentage(updatedCheckingAccountPercentage);
            var customer = customers.stream().filter(x -> x.getId().equals(account.getUserId())).findAny().get();
            if (subscribers.contains(customer)) customer.getNotification("New balance percentage set by bank");
        }
    }
    /**
     * provides transaction
     * @param transaction transaction to be done
     */
    public void makeTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.execute(this);
    }
    /**
     * rollback transaction
     * */
    public void rollbackTransaction(Transaction transaction) {
        if (!transactions.contains(transaction)) return;
        transaction.undo();
    }
}
